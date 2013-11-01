package com.lolRiver.river.persistence;

import com.lolRiver.river.bootstrap.TestAppConfiguration;
import com.lolRiver.river.models.Clip;
import com.lolRiver.river.persistence.interfaces.ClipDao;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/10/13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfiguration.class})
@WebAppConfiguration
public class JdbcClipDaoTest {
    @Autowired
    ClipDao clipDao;

    //@Test
    public void testGetClips() {
        List<Clip> clips = clipDao.getClips(0, 20, "start_time", false);
        System.out.println(clips);
    }
}
