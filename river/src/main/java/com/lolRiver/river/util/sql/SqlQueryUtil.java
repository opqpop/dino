package com.lolRiver.river.util.sql;

import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/1/13
 */

public class SqlQueryUtil {
    private static final Logger LOGGER = Logger.getLogger(SqlQueryUtil.class);

    /**
     * @param paramsToMatch param name for key, conditional operator string for value (<, >, =)
     * @return sql conditional string you can insert into any sql clause ending with "WHERE". The caller must use namedJdbcTemplate.
     */
    public static String conditionalQuery(final Map<String, String> paramsToMatch) {
        final StringBuilder sb = new StringBuilder();
        if (paramsToMatch != null) {
            boolean isFirstParam = true;
            for (final String paramName : paramsToMatch.keySet()) {
                if (isFirstParam) {
                    isFirstParam = false;
                } else {
                    sb.append(" AND ");
                }
                sb.append(String.format("%s %s :%s", paramName, paramsToMatch.get(paramName),
                                       CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, paramName)));
            }
        }
        return sb.toString();
    }

    /**
     * @param list list of items to translate
     * @return (x, y) for use in WHERE IN condition
     */
    public static String inClause(List<String> list) {
        return inClause(list, false);
    }

    /**
     * @param list                    list of items to translate
     * @param encloseWithDoubleQuotes encloses items in quotes if set
     * @return ("x", "y") or (x, y) for use in WHERE IN condition
     */
    public static String inClause(List<String> list, boolean encloseWithDoubleQuotes) {
        if (encloseWithDoubleQuotes) {
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                if (s.length() > 2) {
                    if (s.charAt(0) != '"') {
                        s = '"' + s;
                    }
                    if (s.charAt(s.length() - 1) != '"') {
                        s = s + '"';
                    }
                } else {
                    throw new IllegalArgumentException("Tried to construct SQL in clause using string: " + s);
                }
                list.set(i, s);
            }
        }

        String[] arr = list.toArray(new String[list.size()]);
        String s = Arrays.toString(arr);
        s = s.replace('[', '(');
        s = s.replace(']', ')');
        return s;
    }
}
