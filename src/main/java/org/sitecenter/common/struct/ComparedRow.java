package org.sitecenter.common.struct;

import lombok.Data;

@Data
public class ComparedRow extends ReportRow{
    private ReportRow left;
    private ReportRow right;

    public ComparedRow(String group, String code, String name, String value, int status, String comment) {
        super(group, code, name, value, status, comment);
    }

    public ComparedRow(ReportRow left, ReportRow right) {
        if (left == null && right == null) throw new IllegalArgumentException("One of argument have to be not null.");
        ReportRow reference = (right == null) ? left : right;
        this.group = reference.getGroup();
        this.code = reference.getCode();
        this.name = reference.getName();
        this.value = reference.getValue();
        this.status = reference.getStatus();
        this.comment = reference.getComment();

        this.left = left;
        this.right = right;
    }
}
