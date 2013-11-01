package com.lolRiver.river.multithread;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import com.lolRiver.river.multithread.workerStarters.rawDataWorkerStarters.KassadinWorkerStarter;
import com.lolRiver.river.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class KassadinWorkerStarterTest {
    @Autowired
    KassadinWorkerStarter starter;

    @Test
    public void testCanStart() throws Exception {
        starter.setLastStart(DateUtil.getCurrentTimestamp());
        starter.setLimitedCallIntervalMinutesForTesting(1);
        assertFalse(starter.canStart());

        int minutesToSleep = starter.limitedCallIntervalMinutes();
        Thread.sleep((long)minutesToSleep * 60000);
        assertTrue(starter.canStart());

        starter.resetLimitedCallIntervalMinutesForTesting();
    }

    @Test
    public void testLimitedCallIntervalMinutes() {
        int numMinutesPerDay = 24 * 60;
        int numApiCalls = starter.numTrackedStreamerAccounts() * numMinutesPerDay / starter.limitedCallIntervalMinutes();
        assertTrue(numApiCalls < starter.MAX_API_CALLS_PER_DAY);
    }
}
