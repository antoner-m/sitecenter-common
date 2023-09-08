package org.sitecenter.common.struct;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Report {
    String name;
    LocalDateTime date;
    List<ReportRow> rows = new LinkedList<>();
    Map<String,String> groups = new LinkedHashMap<>();
    String comment;
    public Report(@NonNull String name) {
        this.name = name;
        this.date = LocalDateTime.now();
    }
    public Report addRow(@NonNull String group, @NonNull String code, @NonNull String name, String value, int status, String comment) {
        synchronized (rows) {
            getRows().add(new ReportRow(group, code, name, value, status, comment));
        }
        return this;
    }
    public Report addError(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        synchronized (rows) {
            getRows().add(new ReportRow(group, code, name, value, ReportRow.STATUS_ERROR, comment));
        }
        return this;
    }
    public Report addOk(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        synchronized (rows) {
            getRows().add(new ReportRow(group, code, name, value, ReportRow.STATUS_OK, comment));
        }
        return this;
    }
    public Report addInfo(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        synchronized (rows) {
            getRows().add(new ReportRow(group, code, name, value, ReportRow.STATUS_INFO, comment));
        }
        return this;
    }
    public Report addWarn(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        synchronized (rows) {
            getRows().add(new ReportRow(group, code, name, value, ReportRow.STATUS_WARN, comment));
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
