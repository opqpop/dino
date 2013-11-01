package com.lolRiver.river.multithread.workers;

import com.lolRiver.river.controllers.converters.VideoToClipConverter;
import com.lolRiver.river.models.Clip;
import com.lolRiver.river.models.State;
import com.lolRiver.river.models.Video;
import com.lolRiver.river.persistence.DaoCollection;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/2/13
 */

public class VideoToClipWorker extends Worker {
    private static final Logger LOGGER = Logger.getLogger(VideoToClipWorker.class.getName());

    private VideoToClipConverter converter;
    private Video video;
    private DaoCollection daoCollection;

    public VideoToClipWorker(Video video, VideoToClipConverter converter, DaoCollection daoCollection) {
        if (video == null) {
            throw new IllegalArgumentException("video cannot be null when instantiating VideoToClipWorker");
        } else if (converter == null) {
            throw new IllegalArgumentException("converter cannot be null when instantiating VideoToClipWorker");
        } else if (daoCollection == null) {
            throw new IllegalArgumentException("daoCollection cannot be null when instantiating VideoToClipWorker");
        }

        this.video = video;
        this.converter = converter;
        this.daoCollection = daoCollection;
    }

    public boolean lockVideo() {
        boolean claimedLockOnVideo = daoCollection.getVideoDao().updateVideoState(video.getId(), State.PROCESSING);
        return claimedLockOnVideo;
    }

    @Override
    public void run() {
        LOGGER.debug("Attempting to claim: " + video.getId());

        if (!lockVideo()) {
            /* the video was stolen by another thread.
             * return, and allow the request consumer to spin up a new thread with a new thread request
             */
            LOGGER.debug("Could not claim video: " + video.getId() +
                    ". It has already been claimed.");
            return;
        }

        LOGGER.debug("Claimed video: " + video.getId());

        if (converter.canConvert(video)) {
            try {
                List<Clip> clips = converter.convert(video);
                if (clips == null || clips.isEmpty()) {
                    // TODO xia look into this problem, because i use min video date for all users,
                    // but some users started being track at different dates
                    // LOGGER.error("converted video to 0 clips: " + video);
                    daoCollection.getVideoDao().updateVideoState(video.getId(), State.ERROR);
                } else {
                    for (Clip clip : clips) {
                        if (clip.isViewable()) {
                            LOGGER.debug("Trying to insert clip: " + clip);
                            daoCollection.getClipDao().insertClip(clip);
                        }
                    }
                    LOGGER.info("converted video to " + clips.size() + " clips");
                    daoCollection.getVideoDao().updateVideoState(video.getId(), State.FINISHED);
                }
            } catch (EmptyResultDataAccessException e) {
                daoCollection.getVideoDao().updateVideoState(video.getId(), State.ERROR);
            } catch (Exception e) {
                LOGGER.error("Couldn't convert video: " + video + " " + e, e);
                daoCollection.getVideoDao().updateVideoState(video.getId(), State.ERROR);
            }
        } else {
            LOGGER.debug("video is not ready to be converted because too recent: " + video);
            LOGGER.debug("reseting back to QUEUED to be processed again at later time");
            daoCollection.getVideoDao().updateVideoState(video.getId(), State.QUEUED);
        }
    }
}
