package org.sitecenter.common.struct;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PairString {
    private String key;
    private String value;

    public PairString() {
    }
    public PairString(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
