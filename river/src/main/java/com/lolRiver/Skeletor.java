package com.lolRiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Primary Skeletor class.
 * Uses Spring's environment property lookup mechanism (see AppConfiguration for property file used).
 * 
 * Spring creates and manages a singleton instance of this class. Do NOT instantiate this class yourself; use 
 * @Autowired to inject it into the classes that need it.
 * 
 * User: dbreen / traack
 * Date: 8/28/13
 */
@Component
public class Skeletor {

    @Autowired
    private Environment env;
    
    // Default value
    private boolean verboseInProduction = false;

    @PostConstruct
    public void init() {
        verboseInProduction = Boolean.valueOf(env.getProperty("skeletor.verbose_production_status"));
    }

    public boolean isVerboseInProduction() {
        return verboseInProduction;
    }

    public boolean isTerseInProduction() {
        return !verboseInProduction;
    }
    
    public boolean isProduction() {
        return "production".equals(System.getProperty("merchantdata.environment"));
    }

    public String getHeartbeatPath() {
        return env.getProperty("skeletor.heartbeat_path");
    }
}
