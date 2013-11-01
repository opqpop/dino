package com.lolRiver.river.controllers;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import com.lolRiver.river.controllers.fetchers.videoFetchers.TwitchVideoFetcher;
import com.lolRiver.river.models.Streamer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class TwitchVideoFetcherTest {
    @Autowired
    private TwitchVideoFetcher fetcher;

    @Test
    public void dummyTest() {
        // TODO xia replace this with real tests
    }

    //@Test
    public void testFetchVideos() throws Exception {
        List<Streamer> streamers = new ArrayList<Streamer>();
        streamers.add(new Streamer("zekent"));
        fetcher.fetchAndStoreVideos(streamers);
    }
}
