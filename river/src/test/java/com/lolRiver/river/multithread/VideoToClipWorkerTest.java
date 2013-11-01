package com.lolRiver.river.multithread;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import com.lolRiver.river.controllers.converters.VideoToClipConverter;
import com.lolRiver.river.models.Video;
import com.lolRiver.river.multithread.workers.VideoToClipWorker;
import com.lolRiver.river.persistence.DaoCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * @author mxia (mxia@lolRiver.com)
 *         10/3/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class VideoToClipWorkerTest {
    @Autowired
    DaoCollection daoCollection;
    @Autowired
    VideoToClipConverter converter;

    @Test
    public void test() {
        List<Video> videos = daoCollection.getVideoDao().getUncovertedVideos();
        for (Video video : videos) {
            VideoToClipWorker worker = new VideoToClipWorker(video, converter, daoCollection);
            worker.run();
        }
    }
}
