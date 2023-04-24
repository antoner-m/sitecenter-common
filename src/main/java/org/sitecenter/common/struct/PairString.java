package org.sitecenter.common.struct;

import lombok.Data;

@Data
public class PairString {
    private String key;
    private String value;

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
