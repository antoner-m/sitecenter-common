package org.sitecenter.common.struct;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    @Test
    void finished() throws InterruptedException {
        Report rpt = new Report();
        Thread.sleep(100);
        rpt.finished();
        long duration = rpt.getGenerationMs();
        assert(duration > 100);
        assert(duration < 300);
    }
    @Test
    void sort() {
        Report rpt = new Report();
        rpt.addRow(ReportRow.error("PARENT","IDX10","Some test 10","Error10", "no comment").setSort(10));
        rpt.addRow(ReportRow.error("PARENT2","IDX20","Some test 20","Error20", "no comment").setSort(20));
        rpt.addRow(ReportRow.error("PARENT","IDX1","Some test1","Error1", "no comment").setSort(1));
        rpt.finished();
        List<ReportRow> rows = rpt.getRows();
        assert ("IDX1".equals(rows.get(0).getCode()));
    }

    @Test
    void haveErrors() {
        Report rpt = new Report();
        rpt.addRow(ReportRow.ok("PARENT","IDX10","Some test 10","Error10", "no comment").setSort(10));
        rpt.addRow(ReportRow.ok("PARENT2","IDX20","Some test 20","Error20", "no comment").setSort(20));
        rpt.addRow(ReportRow.error("PARENT","IDX1","Some test1","Error1", "no comment").setSort(1));
        rpt.finished();
        assert (rpt.haveErrors());
        assert (rpt.haveWarnings());
    }
    @Test
    void haveWarnings() {
        Report rpt = new Report();
        rpt.addRow(ReportRow.ok("PARENT","IDX10","Some test 10","Error10", "no comment").setSort(10));
        rpt.addRow(ReportRow.ok("PARENT2","IDX20","Some test 20","Error20", "no comment").setSort(20));
        rpt.addRow(ReportRow.warn("PARENT","IDX1","Some test1","Error1", "no comment").setSort(1));
        rpt.finished();
        assert (!rpt.haveErrors());
        assert (rpt.haveWarnings());
    }

}