package com.lolRiver.river.controllers.deserializers.videoDeserializers;

import com.jayway.jsonpath.JsonPath;
import com.lolRiver.river.models.Video;
import com.lolRiver.river.util.DateUtil;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mxia (mxia@lolRiver.com)
 *         9/28/13
 */

@Component
public class TwitchVideoDeserializer implements VideoDeserializer {
    private final String VIDEOS_PATH = "$.videos";
    private final String URL_PATH = "url";
    private final String LENGTH_PATH = "length";
    private final String VIEWS_PATH = "views";
    private final String START_TIME_PATH = "recorded_at";

    private String videoPath(int videoIndex) {
        return VIDEOS_PATH + "[" + videoIndex + "]";
    }

    private String url(int videoIndex, String json) {
        return JsonPath.read(json, videoPath(videoIndex) + "." + URL_PATH);
    }

    private int length(int videoIndex, String json) {
        return JsonPath.read(json, videoPath(videoIndex) + "." + LENGTH_PATH);
    }

    private int views(int videoIndex, String json) {
        return JsonPath.read(json, videoPath(videoIndex) + "." + VIEWS_PATH);
    }

    private Timestamp startTime(int videoIndex, String json) throws Exception {
        String startTime = JsonPath.read(json, videoPath(videoIndex) + "." + START_TIME_PATH);
        startTime = startTime.replace('T', ' ');
        startTime = startTime.replace('Z', ' ');
        return Timestamp.valueOf(startTime);
    }

    @Override
    public List<Video> videosFromJson(String json) throws Exception {
        List<Video> videos = new ArrayList<Video>();
        List videosFromJson = JsonPath.read(json, VIDEOS_PATH);
        for (int i = 0; i < videosFromJson.size(); i++) {
            Video video = new Video();
            video.setStartTime(startTime(i, json));
            video.setLength(length(i, json));
            video.setEndTime(DateUtil.addSecondsToTimestamp(video.getLength(), video.getStartTime()));
            video.setUrl(url(i, json));
            video.setViews(views(i, json));
            videos.add(video);
        }
        return videos;
    }
}
