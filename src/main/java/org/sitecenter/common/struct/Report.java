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
}
