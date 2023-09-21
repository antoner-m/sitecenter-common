package org.sitecenter.common.struct;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
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
    @Test
    void duplicatedCode() {
        ReportGroup group1 = new ReportGroup("a", "a group");
        group1.ok("ok","ok", "ok", "");

        assertDoesNotThrow(() -> {
            group1.ok("ok","ok", "ok2", "");
        });
        assertEquals(2, group1.getRows().size());

        ReportGroup group2 = new ReportGroup("a", "a group");
        group2.ok("ok","ok", "ok", "");
        List<ComparedRow> compare = group1.compare(group2);
        assertNotNull(compare);
        assertEquals(1, compare.size());
        group2.ok("ok","ok", "ok2", "");
        compare = group1.compare(group2);
        assertEquals(0, compare.size());
    }
    @Test
    void comparingMultiCode() {
        ReportGroup group1 = new ReportGroup("a", "a group");
        group1.ok("ok","ok", "ok", "");
        group1.ok("ok","ok", "ok2", "");

        ReportGroup group2 = new ReportGroup("a", "a group");
        group2.ok("ok","ok", "ok", "");
        List<ComparedRow> compare = group1.compare(group2);
        assertNotNull(compare);
        assertEquals(1, compare.size());
        group2.ok("ok","ok", "ok2", "");
        compare = group1.compare(group2);
        assertEquals(0, compare.size());

        ReportGroup group3 = new ReportGroup("a", "a group");
        group3.ok("ok","ok", "ok", "");
        group3.error("ok","ok", "ok2", "");
        List<ComparedRow> compare3 = group1.compare(group3);
        assertEquals(1, compare3.size());
    }

    @Test
    void comparingResult() {
        ReportGroup group1 = new ReportGroup("a", "a group");
        group1.ok("idx1","ok", "ok", "");
        group1.ok("idx2","ok", "ok2", "");
        group1.error("idx3","ok", "error", "");

        ReportGroup group2 = new ReportGroup("a", "a group");
        group2.warn("idx1","ok", "ok", "");
        group2.error("idx2","ok", "ok2", "");
        group2.error("idx3","ok", "error2", "");

        List<ComparedRow> compare = group1.compare(group2);
        assertNotNull(compare);
        assertEquals(3, compare.size());
        log.info(compare.toString());

    }


//        group1.setStrictCodes(false);
//        group1.ok("ok","ok", "ok1", "");
//        ReportRow ok_1 = group1.getRow("ok_1").orElse(null);
//        assertNotNull(ok_1);
//        assertEquals("ok1", ok_1.getValue());
//        group1.ok("ok","ok", "ok2", "");
//        group1.ok("ok","ok", "ok3", "");
//        group1.ok("ok","ok", "ok4", "");
//        group1.ok("ok","ok", "ok5", "");

}