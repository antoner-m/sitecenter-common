package org.sitecenter.common.struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ReportRow {
    public final static int STATUS_OK = 0;
    public final static int STATUS_INFO = 1;
    public final static int STATUS_WARN = 2;
    public final static int STATUS_ERROR = 3;
    String group;
    String name;
    String value;
    int status;
    String comment;
}
