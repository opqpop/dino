package com.lolRiver.river.util;

/**
 * @author mxia (mxia@lolRiver.com)
 *         8/19/13
 */

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Clob;

public class DbUtil {
    public static String clobToString(Clob clob) throws Exception {
        if (clob == null) {
            return null;
        }
        InputStream in = clob.getAsciiStream();
        StringWriter w = new StringWriter();
        IOUtils.copy(in, w);
        return w.toString();
    }
}
