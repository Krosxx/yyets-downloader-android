package org.apache.commons.lang3;

/**
 * Created by Vove on 2020/3/17
 */
public class StringUtils {
    public static boolean isBlank(String s) {
        if (s == null) {
            return true;
        }
        return s.trim().length() == 0;
    }
    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        return s.length() == 0;
    }
}
