package com.lolRiver.config;

import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
/**
 * @author mxia@lolRiver.com
 *         10/10/13
 */

public class ConfigMap {
    public Map<String, Object> map;

    public ConfigMap() {
        this("river.config.yml");
    }

    public ConfigMap(String fileName) {
        Yaml yaml = new Yaml();
        Map<String, Object> config = (Map<String, Object>)yaml.load(this.getClass().getResourceAsStream(fileName));
        // TODO make this customizable for all, development, qa, prod, etc.
        this.map = new HashMap<String, Object>((Map<String, Object>)config.get("all"));
    }

    private ConfigMap(String key, Map<String, Object> map) {
        Map<String, Object> newMap = (Map<String, Object>)map.get(key);
        if (newMap == null) {
            throw new IllegalArgumentException("no configmap exists for key: " + key);
        }
        this.map = new HashMap<String, Object>(newMap);
    }

    public ConfigMap getConfigMap(String key) {
        int pathBreak = key.indexOf('.');
        if (pathBreak != -1) {
            ConfigMap tmp = new ConfigMap(key.substring(0, pathBreak), map);
            return tmp.getConfigMap(key.substring(pathBreak + 1));
        }

        return new ConfigMap(key, map);
    }

    public Integer getInteger(String key) {
        ConfigMap tmp = this;
        int pathBreak = key.lastIndexOf('.');
        if (pathBreak != -1) {
            tmp = getConfigMap(key.substring(0, pathBreak));
            key = key.substring(pathBreak + 1);
        }
        return (Integer)tmp.map.get(key);
    }

    public Double getDouble(String key) {
        ConfigMap tmp = this;
        int pathBreak = key.lastIndexOf('.');
        if (pathBreak != -1) {
            tmp = getConfigMap(key.substring(0, pathBreak));
            key = key.substring(pathBreak + 1);
        }
        return ((Integer)tmp.map.get(key)).doubleValue();
    }

    public Long getLong(String key) {
        ConfigMap tmp = this;
        int pathBreak = key.lastIndexOf('.');
        if (pathBreak != -1) {
            tmp = getConfigMap(key.substring(0, pathBreak));
            key = key.substring(pathBreak + 1);
        }
        return ((Integer)tmp.map.get(key)).longValue();
    }

    public String getString(String key) {
        ConfigMap tmp = this;
        int pathBreak = key.lastIndexOf('.');
        if (pathBreak != -1) {
            tmp = getConfigMap(key.substring(0, pathBreak));
            key = key.substring(pathBreak + 1);
        }
        return (String)tmp.map.get(key);
    }

    public Boolean getBoolean(String key) {
        ConfigMap tmp = this;
        int pathBreak = key.lastIndexOf('.');
        if (pathBreak != -1) {
            tmp = getConfigMap(key.substring(0, pathBreak));
            key = key.substring(pathBreak + 1);
        }
        return (Boolean)tmp.map.get(key);
    }

    @Override
    public String toString() {
        return "ConfigMap{" +
               "map=" + map +
               '}';
    }
}
