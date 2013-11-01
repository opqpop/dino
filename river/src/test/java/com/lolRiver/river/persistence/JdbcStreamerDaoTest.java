package com.lolRiver.river.persistence;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import com.lolRiver.river.models.Streamer;
import com.lolRiver.river.persistence.interfaces.StreamerDao;
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
 *         10/2/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class JdbcStreamerDaoTest {
    @Autowired
    StreamerDao streamerDao;

    @Test
    public void testInsertStreamers() {
        List<Streamer> list = new ArrayList<Streamer>();
        list.add(new Streamer("tsm_dyrus"));
        for (Streamer streamer : list) {
            streamerDao.insertStreamer(streamer);
        }
    }
}
