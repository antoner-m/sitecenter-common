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
    String name;
    LocalDateTime date;
    /** Time to generate report. */
    long generationMs;

    List<ReportRow> rows = new LinkedList<>();
    Map<String,String> groups = new LinkedHashMap<>();
    String comment;
    public Report() {
        this.name = "";
        this.date = LocalDateTime.now(ZoneOffset.UTC);
    }
    public Report(@NonNull String name) {
        this.name = name;
        this.date = LocalDateTime.now(ZoneOffset.UTC);
    }
    public Report addRow(ReportRow row) {
        synchronized (rows) {
            getRows().add(row);
        }
        return this;
    }

    public Report addGroup(@NonNull String code, @NonNull  String name) {
        synchronized (groups) {
            getGroups().put(code, name);
        }
        return this;
    }
    public ReportGroup rowsByGroup(@NonNull String groupCode) {
        synchronized (groups) {
            List<ReportRow> groupRows = getRows().stream().filter(row -> groupCode.equals(row.getGroup())).collect(Collectors.toList());
            return new ReportGroup(groupCode, getGroups().get(groupCode), groupRows);
        }
    }
    public List<ReportGroup> reportGroups() {
        synchronized (groups) {
            initGroups();
            return getGroups().keySet().stream().map(this::rowsByGroup).collect(Collectors.toList());
        }
    }

    public void sortRows() {
        synchronized (rows) {
            getRows().sort(Comparator.comparing(ReportRow::getSort));
        }
    }
    public void finished() {
        sortRows();
        long millis = date.toInstant(ZoneOffset.UTC).toEpochMilli();
        long now = LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC).toEpochMilli();
        setGenerationMs(now-millis);
    }
    public boolean haveErrors() {
        return getRows().stream().anyMatch(x -> x.getStatus() == ReportRow.STATUS_ERROR);
    }
    public boolean haveWarnings() {
        return getRows().stream().anyMatch(x -> x.getStatus() == ReportRow.STATUS_WARN || x.getStatus() == ReportRow.STATUS_ERROR);
    }

    /** Init group codes from rows in case when reporter forget to init group codes/names */
    private void initGroups() {
        synchronized (groups) {
            for (ReportRow row : rows) {
                String groupCode = row.getGroup();
                if (!getGroups().containsKey(groupCode))
                    getGroups().put(groupCode, "");
            }
        }
    }
}
