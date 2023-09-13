package org.sitecenter.common.struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ComparedRow> compare(ReportGroup comparingReportGroup) {
        List<ComparedRow> result = new ArrayList<>();
        for (ReportRow row : getRows()) {
            ReportRow comparingRow = comparingReportGroup.getRows().stream().filter(x -> row.getGroup().equalsIgnoreCase(x.getGroup()) &&
                    row.getCode().equalsIgnoreCase(x.getCode()))
                    .findAny().orElse(null);
            if (comparingRow == null) {
                result.add(new ComparedRow(row, comparingRow));
                continue;
            }
            if (comparingRow.getStatus() != row.getStatus() ||
                    !comparingRow.getValue().equals(row.getValue())) {
                result.add(new ComparedRow(row, comparingRow));
            }
        }
        //Search comparingReport for codes that not exists in our report.
        for (ReportRow comparingRow : comparingReportGroup.getRows()) {
            ReportRow row = getRows().stream().filter(x -> comparingRow.getGroup().equalsIgnoreCase(x.getGroup()) &&
                    comparingRow.getCode().equalsIgnoreCase(x.getCode()))
                    .findAny().orElse(null);
            if (row == null)
                result.add(new ComparedRow(comparingRow, null));
        }
        return result;
    }

    public boolean haveErrors() {
        return getRows().stream().anyMatch(x -> x.getStatus() == ReportRow.STATUS_ERROR);
    }

    public boolean haveWarnings() {
        return getRows().stream().anyMatch(x -> x.getStatus() == ReportRow.STATUS_WARN || x.getStatus() == ReportRow.STATUS_ERROR);
    }
}
