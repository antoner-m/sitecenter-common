package org.sitecenter.common.struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ReportGroup {
    String code;
    String name;
    String comment;
    int status;
    boolean finished = false;
    boolean ok = true;

    List<ReportGroup> groups = new ArrayList<>();

    List<ReportRow> rows = new ArrayList<>();
//    boolean strictCodes = true;

    public ReportGroup(String code, String name) {
        this.code = code;
        this.name = name;
    }

//    public ReportGroup(String code, String name, boolean strictCodes) {
//        this.code = code;
//        this.name = name;
//        this.strictCodes = strictCodes;
//    }

    public Optional<ReportRow> getRow(@NonNull String code) {
        return rows.stream().filter(x -> code.equalsIgnoreCase(x.getCode())).findAny();
    }
    public List<ReportRow> getRows(@NonNull String code) {
        return rows.stream().filter(x -> code.equalsIgnoreCase(x.getCode())).collect(Collectors.toList());
    }
    // -----------------------------------------------------------------------------------------------------------------


    public List<ComparedRow> compare(ReportGroup comparingReportGroup) {
        List<ComparedRow> result = new ArrayList<>();
        List <String> codes = getRows().stream().map(ReportRow::getCode).collect(Collectors.toList());
        List <String> comparingCodes = comparingReportGroup.getRows().stream().map(ReportRow::getCode).collect(Collectors.toList());

        Set<String> allCodes = new LinkedHashSet();
        allCodes.addAll(codes);
        allCodes.addAll(comparingCodes);


        for (String code : allCodes) {
            List <ReportRow> thisRows = getRows(code);
            List <ReportRow> comparingRows = comparingReportGroup.getRows(code);
            int maxSize = Math.max(thisRows.size(), comparingRows.size());
            for (int i=0; i<maxSize; i++) {
                ReportRow thisRow = (i >= thisRows.size()) ? null : thisRows.get(i);
                ReportRow comparingRow = (i >= comparingRows.size()) ? null : comparingRows.get(i);
                if (thisRow == null || comparingRow == null) {
                    result.add(new ComparedRow(thisRow, comparingRow));
                    continue;
                }
                if (comparingRow.getStatus() != thisRow.getStatus() ||
                        !comparingRow.getValue().equals(thisRow.getValue())) {
                    result.add(new ComparedRow(thisRow, comparingRow));
                }
            }
        }
        return result;
    }

    public boolean haveErrors() {
        return getRows().stream().anyMatch(x -> x.getStatus() == ReportRow.STATUS_ERROR);
    }

    public boolean haveWarnings() {
        return getRows().stream().anyMatch(x -> x.getStatus() == ReportRow.STATUS_WARN || x.getStatus() == ReportRow.STATUS_ERROR);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public ReportRow addRow(ReportRow row) {
        if (row.getCode() == null) throw new ReportException("Code is null.");
        if (row.getGroup() == null) throw new ReportException("Group is null.");
        if (!getCode().equalsIgnoreCase(row.getGroup())) throw new ReportException("Group is not equal.");

        synchronized (rows) {
//            if (rows.stream().anyMatch(x -> row.getCode().equalsIgnoreCase(x.getCode()))) {
//                if (strictCodes)
//                    throw new ReportException(String.format("Code:%s for Group:%s already defined in report group!", row.getCode(), row.getGroup()));
//                final String new_code = row.getCode();
//                List<String> codes = rows.stream().filter(x -> x.getCode().length() > new_code.length() + 1).filter(x -> x.getCode().startsWith(row.getCode())).map(x -> x.getCode().substring(new_code.length() + 1)).collect(Collectors.toList());
//                int maxIdx = 0;
//                for (String codeFound : codes)
//                    try {
//                        int val = Integer.parseInt(codeFound);
//                        if (val > maxIdx) maxIdx = val;
//                    } catch (Exception ex) {
//                        //ignore
//                    }
//                row.setCode(new_code + "_" + (maxIdx + 1));
//            }
            rows.add(row);
        }
        return row;
    }

    public ReportRow error(@NonNull String code, @NonNull String name, String value, String comment) {
        return addRow(ReportRow.error(getCode(), code, name, value, comment));
    }

    public ReportRow ok(@NonNull String code, @NonNull String name, String value, String comment) {
        return addRow(ReportRow.ok(getCode(), code, name, value, comment));
    }

    public ReportRow info(@NonNull String code, @NonNull String name, String value, String comment) {
        return addRow(ReportRow.info(getCode(), code, name, value, comment));
    }

    public ReportRow warn(@NonNull String code, @NonNull String name, String value, String comment) {
        return addRow(ReportRow.warn(getCode(), code, name, value, comment));
    }
}
