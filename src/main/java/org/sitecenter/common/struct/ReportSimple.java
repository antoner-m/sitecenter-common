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
public class ReportSimple {
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

    List<ReportGroup> groups = new ArrayList<>();
    String comment;

    public ReportSimple() {
        this.uuid = UUID.randomUUID().toString();
        this.code = null;
        this.name = "";
        this.date = LocalDateTime.now(ZoneOffset.UTC);
    }

    public ReportSimple(@NonNull String name) {
        this.uuid = UUID.randomUUID().toString();
        this.code = null;
        this.name = name;
        this.date = LocalDateTime.now(ZoneOffset.UTC);
    }

    public ReportSimple(@NonNull String code, @NonNull String name) {
        this.uuid = UUID.randomUUID().toString();
        this.code = code;
        this.name = name;
        this.date = LocalDateTime.now(ZoneOffset.UTC);
    }

    public ReportSimple(@NonNull Report complex) {
        this.uuid = complex.getUuid();
        this.code = complex.getCode();
        this.name = complex.getName();
        this.date = complex.getDate();
        this.generationMs = complex.getGenerationMs();
        this.finished = complex.isFinished();
        this.ok = complex.isOk();
        this.comment = complex.getComment();

        // Copy groups from the Map in Report to the List in ReportSimple
        if (complex.getGroups() != null) {
            this.groups = new ArrayList<>(complex.getGroups().values());
        } else {
            this.groups = new ArrayList<>();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public ReportSimple addRow(ReportRow row) {
        if (row.getGroup() == null) throw new ReportException("Group is null.");

        synchronized (groups) {
            ReportGroup reportGroup = findOrCreateGroup(row.getGroup());
            reportGroup.addRow(row);
        }
        return this;
    }
    public ReportSimple error(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        ReportGroup reportGroup = findOrCreateGroup(group);
        if (reportGroup == null) return null;
        reportGroup.error(code, name, value, comment);
        return this;
    }

    public ReportSimple ok(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        ReportGroup reportGroup = findOrCreateGroup(group);
        if (reportGroup == null) return null;
        reportGroup.ok(code, name, value, comment);
        return this;
    }

    public ReportSimple info(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        ReportGroup reportGroup = findOrCreateGroup(group);
        if (reportGroup == null) return null;
        reportGroup.info(code, name, value, comment);
        return this;
    }

    public ReportSimple warn(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        ReportGroup reportGroup = findOrCreateGroup(group);
        if (reportGroup == null) return null;
        reportGroup.warn(code, name, value, comment);
        return this;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void addGroup(@NonNull ReportGroup group) {
        synchronized (groups) {
            getGroups().add(group);
        }
    }
    public void addGroup(@NonNull List<ReportGroup> addGroups) {
        synchronized (groups) {
            for (ReportGroup group : addGroups) {
                getGroups().add(group);
            }
        }
    }
    public ReportGroup addGroup(@NonNull String code, @NonNull String name) {
        synchronized (groups) {
            ReportGroup group = new ReportGroup(code, name);
            getGroups().add(group);
            return group;
        }
    }

    public ReportGroup findGroup(@NonNull String groupCode) {
        synchronized (groups) {

            return groups.stream().filter(g->g.getCode().equalsIgnoreCase(groupCode)).findAny().orElse(null);
        }
    }
    public ReportGroup findOrCreateGroup(@NonNull String groupCode) {
        synchronized (groups) {
            ReportGroup group = findGroup(groupCode);
            if (group != null) return group;
            return addGroup(groupCode, "");
        }
    }

    public List<ReportGroup> reportGroups() {
        synchronized (groups) {
            return new ArrayList<>(getGroups());
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    public List<ReportRow> allRows() {
        synchronized (groups) {
            return getGroups().stream().flatMap(x -> x.getRows().stream()).collect(Collectors.toList());
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
            for (ReportGroup group : getGroups()) {
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
        return getGroups().stream().anyMatch(ReportGroup::haveErrors);
    }

    public boolean haveWarnings() {
        return getGroups().stream().anyMatch(ReportGroup::haveWarnings);
    }

    public List<ReportRow> compare(@NonNull ReportSimple report) {
        List<ReportRow> result = new ArrayList<>();
        for (ReportGroup group : this.getGroups()) {
            ReportGroup comparedGroup = report.findGroup(group.getCode());
            List<ComparedRow> resultCompare = group.compare(comparedGroup);
            result.addAll(resultCompare);
        }
        return result;
    }

    public void setGroups(Map<String, ReportGroup> groups) {
        this.groups.clear();
        this.groups = new ArrayList<>(groups.values());
    }
    public void setGroups(List<ReportGroup> groups) {
        this.groups.clear();
        this.groups = groups;
    }


    public void sanitizeGroupKeys() {
        synchronized (groups) {
            List <ReportGroup> groups = getGroups();

            if (groups == null || groups.isEmpty()) {
                return;
            }

            List<ReportGroup> sanitized = new ArrayList<>( groups.size());
            for (ReportGroup entry : groups) {
                String sanitizedKey = entry.getCode().replaceAll("[:,\\.]", "_");
                entry.setCode(sanitizedKey);
                sanitized.add(entry);
                for (ReportRow row : entry.getRows()) {
                    if (row.getGroup() == null)
                        continue;
                    String sanitizedGroup = row.getGroup().replaceAll("[:,\\.]", "_");
                    row.setGroup(sanitizedGroup);
                }
            }
            setGroups(sanitized);
        }
    }
}
