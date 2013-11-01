package com.lolRiver.river.models;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/28/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class KassadinGameDeserializerTest {
    @Test
    public void dummyTest() {
        // TODO xia replace this with real tests
    }
}