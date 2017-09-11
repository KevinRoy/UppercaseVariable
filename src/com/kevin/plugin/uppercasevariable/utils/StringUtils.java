package com.kevin.plugin.uppercasevariable.utils;

import org.apache.http.util.TextUtils;

public class StringUtils {

    public static String getJavaString(String s) {
        if (TextUtils.isEmpty(s))
            return "";

        return "public static final String ".concat(s.toUpperCase()).concat(" = ") + "\"" + s + "\"".concat(";");
    }

    public static String getKotlinString(String s) {
        if (TextUtils.isEmpty(s))
            return "";

        return "val ".concat(s.toUpperCase()).concat(" = ") + "\"" + s + "\"";
    }
}
