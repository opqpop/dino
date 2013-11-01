package com.lolRiver.river.util;

/**
 * @author mxia (mxia@lolRiver.com)
 *         10/3/13
 */

public class NumbersUtil {
    /**
     * only works for I-V right now (league division)
     */
    public static String romanNumeralToNumber(String s) {
        if (s.equals("I")) {
            return "1";
        } else if (s.equals("II")) {
            return "2";
        } else if (s.equals("III")) {
            return "3";
        } else if (s.equals("IV")) {
            return "4";
        } else if (s.equals("V")) {
            return "5";
        }
        return "";
    }
}
