package com.lolRiver.river.models;

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/30/13
 */

public class Video {
    public static final String ID_STRING = "id";
    public static final String STREAMER_NAME_STRING = "streamer_name";
    public static final String URL_STRING = "url";
    public static final String START_TIME_STRING = "start_time";
    public static final String END_TIME_STRING = "end_time";
    public static final String LENGTH_STRING = "length";
    public static final String VIEWS_STRING = "views";

    private int id;
    private String streamerName;
    private String url;
    private Timestamp startTime;
    private Timestamp endTime;
    private int length;             // video length in seconds
    private int views;

    public List<String> invalidFields() {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isBlank(url)) {
            list.add(URL_STRING);
        }
        if (startTime == null) {
            list.add(START_TIME_STRING);
        }
        if (endTime == null) {
            list.add(END_TIME_STRING);
        }
        if (length <= 0) {
            list.add(LENGTH_STRING);
        }
        if (views < 0) {
            list.add(VIEWS_STRING);
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public Video setId(int id) {
        this.id = id;
        return this;
    }

    public String getStreamerName() {
        return streamerName;
    }

    public Video setStreamerName(String streamerName) {
        this.streamerName = streamerName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Video setUrl(String url) {
        this.url = url;
        return this;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Video setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        return this;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Video setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        return this;
    }

    public int getLength() {
        return length;
    }

    public Video setLength(int length) {
        this.length = length;
        return this;
    }

    public int getViews() {
        return views;
    }

    public Video setViews(int views) {
        this.views = views;
        return this;
    }

    @Override
    public String toString() {
        return "Video{" +
               "id=" + id +
               ", streamerName='" + streamerName + '\'' +
               ", url='" + url + '\'' +
               ", startTime=" + startTime +
               ", endTime=" + endTime +
               ", length=" + length +
               ", views=" + views +
               '}';
    }
}
