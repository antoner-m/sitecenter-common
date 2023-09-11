package org.sitecenter.common.struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ReportGroup {
    String code;
    String name;
    List<ReportRow> rows = new ArrayList<>();
    public ReportGroup(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
