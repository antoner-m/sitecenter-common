package org.sitecenter.common.struct;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class Report {
    String uuid;
    String code;
    String name;
    LocalDateTime date;
    /**
     * Time to generate report.
     */
    long generationMs;

    boolean finished = false;
    boolean ok = true;
//    boolean strictCodes = true;

    Map<String, ReportGroup> groups = new LinkedHashMap<>();
    String comment;

    public Report() {
        this.uuid = UUID.randomUUID().toString();
        this.code = null;
        this.name = "";
        this.date = LocalDateTime.now(ZoneOffset.UTC);
    }

    public Report(@NonNull String name) {
        this.uuid = UUID.randomUUID().toString();
        this.code = null;
        this.name = name;
        this.date = LocalDateTime.now(ZoneOffset.UTC);
    }

    public Report(@NonNull String code, @NonNull String name) {
        this.uuid = UUID.randomUUID().toString();
        this.code = code;
        this.name = name;
        this.date = LocalDateTime.now(ZoneOffset.UTC);
    }

    //------------------------------------------------------------------------------------------------------------------
    public Report addRow(ReportRow row) {
        if (row.getGroup() == null) throw new ReportException("Group is null.");

        synchronized (groups) {
            ReportGroup reportGroup = findOrCreateGroup(row.getGroup());
            reportGroup.addRow(row);
        }
        return this;
    }
    public Report error(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        ReportGroup reportGroup = findOrCreateGroup(group);
        if (reportGroup == null) return null;
        reportGroup.error(code, name, value, comment);
        return this;
    }

    public Report ok(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        ReportGroup reportGroup = findOrCreateGroup(group);
        if (reportGroup == null) return null;
        reportGroup.ok(code, name, value, comment);
        return this;
    }

    public Report info(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        ReportGroup reportGroup = findOrCreateGroup(group);
        if (reportGroup == null) return null;
        reportGroup.info(code, name, value, comment);
        return this;
    }

    public Report warn(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        ReportGroup reportGroup = findOrCreateGroup(group);
        if (reportGroup == null) return null;
        reportGroup.warn(code, name, value, comment);
        return this;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void addGroup(@NonNull ReportGroup group) {
        synchronized (groups) {
            if (getGroups().keySet().stream().anyMatch(code::equalsIgnoreCase))
                throw new ReportException(String.format("Report already have group with code:%s!", code));
            getGroups().put(code, group);
        }
    }
    public void addGroup(@NonNull List<ReportGroup> addGroups) {
        synchronized (groups) {
            for (ReportGroup group : addGroups) {
                if (getGroups().keySet().stream().anyMatch(code::equalsIgnoreCase))
                    throw new ReportException(String.format("Report already have group with code:%s!", code));
                getGroups().put(code, group);
            }
        }
    }
    public ReportGroup addGroup(@NonNull String code, @NonNull String name) {
        synchronized (groups) {
            if (getGroups().keySet().stream().anyMatch(code::equalsIgnoreCase))
                throw new ReportException(String.format("Report already have group with code:%s!", code));
            ReportGroup group = new ReportGroup(code, name);
            getGroups().put(code, group);
            return group;
        }
    }

    public ReportGroup findGroup(@NonNull String groupCode) {
        synchronized (groups) {
            return groups.get(groupCode);
        }
    }
    public ReportGroup findOrCreateGroup(@NonNull String groupCode) {
        synchronized (groups) {
            ReportGroup group = groups.get(groupCode);
            if (group != null) return group;
            return addGroup(groupCode, "");
        }
    }

    public List<ReportGroup> reportGroups() {
        synchronized (groups) {
            return new ArrayList<>(getGroups().values());
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    public List<ReportRow> allRows() {
        synchronized (groups) {
            return getGroups().values().stream().flatMap(x -> x.getRows().stream()).collect(Collectors.toList());
        }
    }

    public Optional<ReportRow> getRow(@NonNull String groupCode, @NonNull String code) {
        ReportGroup group = findGroup(groupCode);
        if (group == null) return null;
        return group.getRow(code);
    }

    public Optional<String> getRowValue(@NonNull String groupCode, @NonNull String code) {
        Optional<ReportRow> row = getRow(groupCode, code);
        return row.map(ReportRow::getValue);
    }

    public void sortRows() {
        synchronized (groups) {
            for (ReportGroup group : getGroups().values()) {
                group.getRows().sort(Comparator.comparing(ReportRow::getSort));
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    public void finished() {
        sortRows();
        long millis = date.toInstant(ZoneOffset.UTC).toEpochMilli();
        long now = LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC).toEpochMilli();
        setGenerationMs(now - millis);
        finished = true;
        ok = !haveErrors();
    }

    public boolean haveErrors() {
        return getGroups().values().stream().anyMatch(ReportGroup::haveErrors);
    }

    public boolean haveWarnings() {
        return getGroups().values().stream().anyMatch(ReportGroup::haveWarnings);
    }

    public List<ReportRow> compare(@NonNull Report report) {
        List<ReportRow> result = new ArrayList<>();
        for (ReportGroup thisGroup : this.getGroups().values()) {
            ReportGroup comparedGroup = report.findGroup(thisGroup.getCode());
            List<ComparedRow> resultCompare = thisGroup.compare(comparedGroup);
            result.addAll(resultCompare);
        }
        return result;
    }

    public void setGroups(Map<String, ReportGroup> groups) {
        this.groups.clear();
        this.groups.putAll(groups);
    }
}
