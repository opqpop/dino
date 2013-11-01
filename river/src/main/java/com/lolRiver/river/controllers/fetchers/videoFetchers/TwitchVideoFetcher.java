package com.lolRiver.river.controllers.fetchers.videoFetchers;

import com.jayway.jsonpath.JsonPath;
import com.lolRiver.config.ConfigMap;
import com.lolRiver.river.controllers.deserializers.videoDeserializers.TwitchVideoDeserializer;
import com.lolRiver.river.models.Streamer;
import com.lolRiver.river.models.Video;
import com.lolRiver.river.persistence.interfaces.VideoDao;
import com.lolRiver.river.util.DateUtil;
import com.lolRiver.river.util.WebDataFetcher;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

@Component
public class TwitchVideoFetcher implements VideoFetcher {
    private static final Logger LOGGER = Logger.getLogger(TwitchVideoFetcher.class.getName());

    private ConfigMap configMap;
    private ConfigMap twitchConfigMap;
    private final Timestamp TWITCH_VIDEO_MIN_START_TIME;  // videos older than this won't be read
    private final String RECENT_VIDEOS_FROM_STREAMER_NAME_URL;
    private final String NEXT_PAGE_PATH = "$._links.next";

    @Autowired
    private WebDataFetcher webDataFetcher;

    @Autowired
    private TwitchVideoDeserializer videoDeserializer;

    @Autowired
    private VideoDao videoDao;

    private TwitchVideoFetcher() {
        /* Note: this will throw a runtime exception if MERCHANTDATA_ENV system env variable
        * or merchantdata.environment java property isn't set.
        *
        * pom.xml (under <!-- Jetty Plugin -->) has been configured to set merchantdata.environment when you run mvn jetty:run
        * Just modify that to change your environment.
        *
        * If you want to set the system variable for some reason,
        * you must force mvn to see bash variables by putting
        *          setenv M2_HOME /usr/bin/mvn
        * inside /etc/launchd.conf
        */
        configMap = new ConfigMap();
        twitchConfigMap = configMap.getConfigMap("twitch");
        RECENT_VIDEOS_FROM_STREAMER_NAME_URL = twitchConfigMap.getString("recent_videos_from_streamer_name_url");

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        TWITCH_VIDEO_MIN_START_TIME = DateUtil.dateTimeToTimestamp(formatter.parseDateTime("06/10/2013 17:10:00"));
    }

    @Override
    public void fetchAndStoreVideos(List<Streamer> streamers) {
        for (Streamer streamer : streamers) {
            String url = String.format(RECENT_VIDEOS_FROM_STREAMER_NAME_URL, streamer.getName());
            try {
                fetchAndStoreVideos(streamer.getName(), url);
            } catch (Exception e) {
                LOGGER.error("couldn't fetch stream raw data of streamer: " + streamer + " using url: " + url, e);
            }
        }
    }

    private void fetchAndStoreVideos(String streamerName, String url) throws Exception {
        LOGGER.info("Fetching video for " + streamerName + " url: " + url);
        int numInserted = 0;
        String json = sendGet(url);
        List<Video> videos = videoDeserializer.videosFromJson(json);
        boolean shouldFetchNextVideos = videos != null && !videos.isEmpty();
        for (Video video : videos) {
            List<String> invalidFields = video.invalidFields();
            if (invalidFields.isEmpty()) {
                Video insertedVideo = null;
                if (video.getStartTime().after(TWITCH_VIDEO_MIN_START_TIME)) {
                    video.setStreamerName(streamerName);
                    insertedVideo = videoDao.insertVideo(video);

                    // we never delete videos, so that we can stop when we hit a video that's already in the DB,
                    // meaning we can hackishly specify a min_start_time for each player
                    if (insertedVideo == null) {
                        shouldFetchNextVideos = false;
                    } else {
                        numInserted++;
                    }
                }
                if (shouldFetchNextVideos && insertedVideo == null) {
                    shouldFetchNextVideos = false;
                }
            } else {
                LOGGER.error("Read invalid video from json: " + json + " url: " + url + "streamerName: " + streamerName +
                             "invalidFields: " + invalidFields);
            }
        }
        if (numInserted > 0) {
            LOGGER.info("Inserted " + numInserted + " videos from " + streamerName + " url: " + url);
        }

        if (shouldFetchNextVideos) {
            String nextPageUrl = JsonPath.read(json, NEXT_PAGE_PATH);
            if (StringUtils.isNotBlank(nextPageUrl)) {
                fetchAndStoreVideos(streamerName, nextPageUrl);
            }
        }
    }

    public String sendGet(String url) throws Exception {
        return webDataFetcher.get(url);
    }
}
