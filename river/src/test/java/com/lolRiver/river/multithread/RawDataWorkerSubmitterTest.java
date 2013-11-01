package com.lolRiver.river.multithread;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/3/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class RawDataWorkerSubmitterTest {
    @Autowired
    RawDataWorkerSubmitter workerSubmitter;

    @Test
    public void dummyTest() {
        // TODO xia replace this with real tests
    }

    //@Test
    public void test() {
        workerSubmitter.setup(true);
    }
}
