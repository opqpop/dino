package com.lolRiver.river.multithread.workers.rawDataWorkers;

import com.lolRiver.river.controllers.fetchers.videoFetchers.VideoFetcher;
import com.lolRiver.river.models.Streamer;
import com.lolRiver.river.multithread.workers.Worker;
import com.lolRiver.river.persistence.interfaces.StreamerDao;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author mxia (mxia@lolRiver.com)
 *         9/30/13
 */

public class TwitchWorker extends Worker {
    private static final Logger LOGGER = Logger.getLogger(TwitchWorker.class.getName());

    private VideoFetcher videoFetcher;
    private StreamerDao streamerDao;

    public TwitchWorker(VideoFetcher videoFetcher, StreamerDao streamerDao) {
        if (videoFetcher == null) {
            throw new IllegalArgumentException("videoFetcher cannot be null when instantiating VideoRawDataWorker");
        } else if (streamerDao == null) {
            throw new IllegalArgumentException("streamerDao cannot be null when instantiating VideoRawDataWorker");
        }
        this.videoFetcher = videoFetcher;
        this.streamerDao = streamerDao;
    }

    @Override
    public void run() {
        LOGGER.info("TwitchWorker - run() started");

        List<Streamer> streamers = streamerDao.getStreamers();
        videoFetcher.fetchAndStoreVideos(streamers);

        LOGGER.info("TwitchWorker - run() ended");
    }
}
