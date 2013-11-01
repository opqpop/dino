package com.lolRiver.river.models;

/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

public class Streamer {
    public static final String NAME_STRING = "name";

    private String name;

    public Streamer() {

    }

    public Streamer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Streamer setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Streamer{" +
               "name='" + name + '\'' +
               '}';
    }
}
