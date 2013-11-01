package com.lolRiver.river.models;

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/1/13
 */

public class Elo {
    public enum Name {
        BRONZE5, BRONZE4, BRONZE3, BRONZE2, BRONZE1,
        SILVER5, SILVER4, SILVER3, SILVER2, SILVER1,
        GOLD5, GOLD4, GOLD3, GOLD2, GOLD1,
        PLATINUM5, PLATINUM4, PLATINUM3, PLATINUM2, PLATINUM1,
        DIAMOND5, DIAMOND4, DIAMOND3, DIAMOND2, DIAMOND1,
        CHALLENGER
    }

    public static final String ID_STRING = "id";
    public static final String NAME_STRING = "elo";
    public static final String TIME_STRING = "time";
    public static final String USER_ID_STRING = "user_id";

    private int id;
    private Name name;
    private Timestamp time;
    private int userId;

    public Elo() {

    }

    public Elo(Name name) {
        this.name = name;
    }

    public static Elo fromString(String eloName) {
        if (StringUtils.isBlank(eloName)) {
            return null;
        }
        return new Elo(Elo.Name.valueOf(eloName));
    }

    public List<String> invalidFields() {
        List<String> list = new ArrayList<String>();
        if (name == null || StringUtils.isBlank(name.name())) {
            list.add(NAME_STRING);
        }
        if (time == null) {
            list.add(TIME_STRING);
        }
        return list;
    }

    public static String generalEloFromElo(Elo elo) {
        if (elo == null) {
            return "NONE";
        }

        int ord = elo.name.ordinal();
        if (ord == Name.CHALLENGER.ordinal()) {
            return Name.CHALLENGER.name();
        } else if (ord >= Name.DIAMOND5.ordinal()) {
            return "DIAMOND";
        } else if (ord >= Name.PLATINUM5.ordinal()) {
            return "PLATINUM";
        } else if (ord >= Name.GOLD5.ordinal()) {
            return "GOLD";
        } else if (ord >= Name.SILVER5.ordinal()) {
            return "SILVER";
        } else if (ord >= Name.BRONZE5.ordinal()) {
            return "BRONZE";
        }
        return "NONE";
    }

    public static List<String> dbEloFromGeneralElo(String generalElo) {
        List<String> list = new ArrayList<String>();
        if (generalElo != null) {
            if (generalElo.equals("CHALLENGER")) {
                list.add(generalElo);
            } else {
                for (int i = 1; i <= 5; i++) {
                    list.add(generalElo + i);
                }
            }
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Timestamp getTime() {
        return time;
    }

    public int getUserId() {
        return userId;
    }

    public Elo setName(Name name) {
        this.name = name;
        return this;
    }

    public Elo setId(int id) {
        this.id = id;
        return this;
    }

    public Elo setTime(Timestamp time) {
        this.time = time;
        return this;
    }

    public Elo setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "Elo{" +
               "id=" + id +
               ", name=" + name +
               ", time=" + time +
               ", userId=" + userId +
               '}';
    }
}
