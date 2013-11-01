package com.lolRiver.river.controllers;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
/**
 * @author mxia (mxia@groupon.com)
 *         10/12/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class HomeControllerTest {
    @Autowired
    HomeController controller;

    @Test
    public void testGetSkinNames() throws Exception {
        controller.dummyMethodForTesting();
        controller.dummyMethodForTesting();
    }
}
