package org.sitecenter.common.struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ReportRow {
    public static final int STATUS_OK = 0;
    public static final int STATUS_INFO = 1;
    public static final int STATUS_WARN = 2;
    public static final int STATUS_ERROR = 3;

    String group;
    String code;
    String name;
    String value;
    int status;
    String comment;
    int sort;

    public ReportRow(String group, String code, String name, String value, int status, String comment) {
        this.group = group;
        this.code = code;
        this.name = name;
        this.value = value;
        this.status = status;
        this.comment = comment;
    }

    public static ReportRow error(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        return new ReportRow(group, code, name, value, ReportRow.STATUS_ERROR, comment);
    }

    public static ReportRow ok(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        return new ReportRow(group, code, name, value, ReportRow.STATUS_OK, comment);
    }

    public static ReportRow info(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        return new ReportRow(group, code, name, value, ReportRow.STATUS_INFO, comment);
    }

    public static ReportRow warn(@NonNull String group, @NonNull String code, @NonNull String name, String value, String comment) {
        return new ReportRow(group, code, name, value, ReportRow.STATUS_WARN, comment);
    }

}
