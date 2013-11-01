package com.lolRiver.river.controllers;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/5/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class LolUserUpdaterControllerTest {
    private static final Logger LOGGER = Logger.getLogger(LolUserUpdaterController.class);

    @Autowired
    LolUserUpdaterController controller;

    @Test
    public void testStart() throws Exception {
        controller.start();
    }
}
