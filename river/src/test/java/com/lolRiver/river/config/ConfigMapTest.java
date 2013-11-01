package com.lolRiver.river.config;

import com.lolRiver.config.ConfigMap;
import org.junit.Test;
/**
 * @author mxia (mxia@groupon.com)
 *         10/11/13
 */

public class ConfigMapTest {
    @Test
    public void test() {
        ConfigMap map = new ConfigMap();
        ConfigMap mysql = map.getConfigMap("mysql");
        String s = mysql.getString("host");
        String f = mysql.getString("db");
        System.out.println(mysql);
        System.out.println(s + f);
    }
}
