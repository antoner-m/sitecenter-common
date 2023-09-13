package org.sitecenter.common.struct;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportGroupTest {

    @Test
    void compare() {
        ReportGroup group1 = new ReportGroup("a","a group");
        group1.getRows().add(ReportRow.ok("a","idx1","index 1","value1",""));
        group1.getRows().add(ReportRow.ok("a","idx2","index 2","value2",""));
        group1.getRows().add(ReportRow.ok("b","idx1","index 1","value1b",""));
        group1.getRows().add(ReportRow.ok("b","idx2","index 2","value2b",""));

        ReportGroup group2 = new ReportGroup("a","a group");
        group2.getRows().add(ReportRow.ok("a","idx1","index 1","value1",""));
        group2.getRows().add(ReportRow.ok("a","idx2","index 2","value2",""));
        group2.getRows().add(ReportRow.ok("b","idx1","index 1","value1b",""));
        group2.getRows().add(ReportRow.ok("b","idx2","index 2","value2b",""));

        List<ComparedRow> comparing = group1.compare(group2);
        assert(comparing.size() == 0);
    }
    @Test
    void compare2() {
        ReportGroup group1 = new ReportGroup("a","a group");
        group1.getRows().add(ReportRow.ok("a","idx1","index 1","value1",""));
        group1.getRows().add(ReportRow.ok("a","idx2","index 2","value2",""));
        group1.getRows().add(ReportRow.ok("b","idx1","index 1","value1b",""));
        group1.getRows().add(ReportRow.ok("b","idx2","index 2","value2b",""));

        ReportGroup group2 = new ReportGroup("a","a group");
        group2.getRows().add(ReportRow.ok("a","idx1","index 1","value1",""));
        group2.getRows().add(ReportRow.ok("a","idx2","index 2","value2",""));
        group2.getRows().add(ReportRow.ok("b","idx1","index 1","value1b",""));
        group2.getRows().add(ReportRow.ok("b","idx2","index 2","value2b-changed",""));

        List<ComparedRow> comparing = group1.compare(group2);
        assert(comparing.size() == 1);
        assert("value2b-changed".equals(comparing.get(0).getValue()));
    }

}