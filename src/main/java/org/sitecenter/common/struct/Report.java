package org.sitecenter.common.struct;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    /** Time to generate report. */
    long generationMs;

    final Map<String,ReportGroup> groups = new LinkedHashMap<>();
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
    public Report addRow(ReportRow row) {
        if (row.getCode() == null) throw new ReportException("Code is null.");
        if (row.getGroup() == null) throw new ReportException("Group is null.");
        synchronized (groups) {
            ReportGroup reportGroup = groups.get(row.getGroup());
            if (reportGroup == null) {
                addGroup(row.getGroup(), "");
                reportGroup = groups.get(row.getGroup());
            }

            if (reportGroup.getRows().stream().anyMatch(x -> row.getCode().equalsIgnoreCase(x.getCode())))
                throw new ReportException(String.format("Code:%s for Group:%s already defined in report!", row.getCode(), row.getGroup()));
            reportGroup.getRows().add(row);
        }
        return this;
    }

    public Report addGroup(@NonNull String code, @NonNull  String name) {
        synchronized (groups) {
            if (getGroups().keySet().stream().anyMatch(code::equalsIgnoreCase))
                throw new ReportException(String.format("Report already have group with code:%s!", code));
            getGroups().put(code, new ReportGroup(code, name));
        }
        return this;
    }
    public ReportGroup findGroup(@NonNull String groupCode) {
        synchronized (groups) {
            return groups.get(groupCode);
        }
    }
    public List<ReportGroup> reportGroups() {
        synchronized (groups) {
            return new ArrayList<>(getGroups().values());
        }
    }
    public List<ReportRow> allRows() {
        synchronized (groups) {
            return getGroups().values().stream().flatMap(x -> x.getRows().stream()).collect(Collectors.toList());
        }
    }

    public void sortRows() {
        synchronized (groups) {
            for (ReportGroup group : getGroups().values()) {
                group.getRows().sort(Comparator.comparing(ReportRow::getSort));
            }
        }
    }
    public void finished() {
        sortRows();
        long millis = date.toInstant(ZoneOffset.UTC).toEpochMilli();
        long now = LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC).toEpochMilli();
        setGenerationMs(now-millis);
    }
    public boolean haveErrors() {
        return getGroups().values().stream().anyMatch(ReportGroup::haveErrors);
    }
    public boolean haveWarnings() {
        return getGroups().values().stream().anyMatch(ReportGroup::haveWarnings);
    }

    public List<ReportRow> compare(@NonNull Report report) {
        List<ReportRow> result = new ArrayList<>();
        for (ReportGroup thisGroup: this.getGroups().values()) {
            ReportGroup comparedGroup = report.findGroup(thisGroup.getCode());
            List<ComparedRow> resultCompare = thisGroup.compare(comparedGroup);
            result.addAll(resultCompare);
        }
        return result;
    }
}
