package com.lolRiver.river.controllers;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import com.lolRiver.river.controllers.fetchers.rawDataFetchers.KassadinRawDataFetcher;
import com.lolRiver.river.models.LolUser;
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
public class KassadinRawDataFetcherTest {
    @Autowired
    private KassadinRawDataFetcher fetcher;

    @Test
    public void dummyTest() {
        // TODO xia replace this with real tests
    }

    //@Test
    public void testFetchRawData() throws Exception {
        List<LolUser> lolUsers = new ArrayList<LolUser>();
        lolUsers.add(new LolUser("markdonkey", "mark donkey", "na"));
        fetcher.fetchAndStoreRawData(lolUsers);
    }
}
