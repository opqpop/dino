package com.lolRiver.river.models;

import org.apache.commons.lang.StringUtils;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/1/13
 */

public class LolUser {
    public static final String ID_STRING = "id";
    public static final String USERNAME_STRING = "name";
    public static final String REGION_STRING = "region";
    public static final String STREAMER_NAME_STRING = "streamer_name";

    private int id;
    private String streamerName;
    private String username;
    private String region;

    public LolUser() {

    }

    public LolUser(String streamerName, String username, String region) {
        this.streamerName = streamerName;
        this.username = username;
        this.region = region;
    }

    public String invalidIdField() {
        return id < 0 ? ID_STRING : null;
    }

    public String invalidUsername() {
        return StringUtils.isBlank(username) ? USERNAME_STRING : null;
    }

    public String invalidRegion() {
        return StringUtils.isBlank(region) ? REGION_STRING : null;
    }

    public String invalidStreamerName() {
        return StringUtils.isBlank(streamerName) ? STREAMER_NAME_STRING : null;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRegion() {
        return region;
    }

    public String getStreamerName() {
        return streamerName;
    }

    public LolUser setId(int id) {
        this.id = id;
        return this;
    }

    public LolUser setUserName(String username) {
        this.username = username;
        return this;
    }

    public LolUser setRegion(String region) {
        this.region = region;
        return this;
    }

    public LolUser setStreamerName(String streamerName) {
        this.streamerName = streamerName;
        return this;
    }

    @Override
    public String toString() {
        return "LolUser{" +
               "id=" + id +
               ", streamerName='" + streamerName + '\'' +
               ", username='" + username + '\'' +
               ", region='" + region + '\'' +
               '}';
    }
}
