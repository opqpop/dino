package com.lolRiver.river.multithread.workerStarters;

import com.lolRiver.river.controllers.converters.VideoToClipConverter;
import com.lolRiver.river.models.Video;
import com.lolRiver.river.multithread.workers.VideoToClipWorker;
import com.lolRiver.river.multithread.workers.Worker;
import com.lolRiver.river.persistence.DaoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/2/13
 */

@Component
public class VideoToClipWorkerStarter implements WorkerStarter {
    @Autowired
    private VideoToClipConverter converter;

    @Autowired
    private DaoCollection daoCollection;

    @Override
    public List<Worker> workersToStart() {
        List<Worker> workers = new ArrayList<Worker>();
        List<Video> videos = daoCollection.getVideoDao().getUncovertedVideos();
        for (Video video : videos) {
            workers.add(new VideoToClipWorker(video, converter, daoCollection));
        }
        return workers;
    }

    @Override
    public boolean canStart() {
        List<Video> videos = daoCollection.getVideoDao().getUncovertedVideos();
        if (videos == null) {
            return false;
        }

        if (videos.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public void start() {

    }
}
