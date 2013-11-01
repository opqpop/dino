package com.lolRiver.river.controllers;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import com.lolRiver.river.controllers.fetchers.rawDataFetchers.RiotRawDataFetcher;
import com.lolRiver.river.models.LolUser;
import com.lolRiver.river.persistence.interfaces.LolUserDao;
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
 *         10/6/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class RiotRawDataFetcherTest {
    @Autowired
    RiotRawDataFetcher fetcher;

    @Autowired
    LolUserDao lolUserDao;

    @Test
    public void dummyTest() {
        // TODO xia replace this with real tests
    }

    //@Test
    public void testFetchRawData() throws Exception {
        List<LolUser> lolUsers = new ArrayList<LolUser>();
        lolUsers.add(new LolUser("phil's friend", "Yellowpanda", "na").setId(19863422));
        lolUsers.add(new LolUser("jon", "JonGhost", "na").setId(19369182));
        lolUsers.add(new LolUser("jon", "cynicaldime", "na").setId(24742043));
        fetcher.fetchAndStoreRawData(lolUsers);
    }

    //@Test
    public void testFetchRawDataFromUserDb() throws Exception {
        fetcher.fetchAndStoreRawData(lolUserDao.getLolUsers());
    }
}
