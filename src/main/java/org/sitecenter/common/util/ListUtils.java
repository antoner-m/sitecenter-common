package org.sitecenter.common.util;

import java.util.Collections;
import java.util.List;

public class ListUtils {
    public static List safe( List other ) {
        return other == null ? Collections.EMPTY_LIST : other;
    }
}
