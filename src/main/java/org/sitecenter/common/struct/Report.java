package org.sitecenter.common.struct;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class Report {
    String name;
    LocalDateTime date;
    List<ReportRow> rows = new LinkedList<>();
    String comment;
    public Report(String name) {
        this.name = name;
        this.date = LocalDateTime.now();
    }
    public void addRow(String group, String code, String name, String value, int status, String comment) {
        getRows().add(new ReportRow(group, code, name, value, status, comment));
    }
}
