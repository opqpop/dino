package com.lolRiver.status;

import com.lolRiver.Skeletor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This servlet first checks whether heartbeat.txt exists, then runs any custom health checks you define.
 * If the heartbeat.txt file is missing, or if any health checks fail, HTTP status 404 is returned.  The path
 * to heartbeat.txt can be overridden by the b3.heartbeat_path property.
 */
@Controller
public class HeartbeatServlet {

    // heartbeat path must begin with /
    private String heartbeat = "/heartbeat.txt";
    private String okString = "GRPN";

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatServlet.class);

    // Singleton Skeletor instance injected by Spring
    @Autowired
    private Skeletor skeletor;

    // Singleton HealthChecks instance injected by Spring
    @Autowired
    private HealthChecks healthChecks;

    // Singleton ServletContext instance injected by Spring
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = {"/heartbeat.txt"}, method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public Object doGet() {

        List<HealthCheckResult> healthCheckResults = new ArrayList<HealthCheckResult>();

        // First check if our heartbeat file is present.
        if (!heartbeatFilePresent()) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }

        // BEGIN -- health checks ----------------------------------------------

        // This is an example.  After you add your own, you can clobber this one.
        healthCheckResults.add(healthChecks.testAlwaysTrue());

        // END -- health checks ------------------------------------------------

        healthCheckResults.removeAll(Collections.singleton(null));

        // process health checks
        for (HealthCheckResult r : healthCheckResults) {
            if (r.fail()) {
                int code = r.getHttpCode();

                String exmsg = "";
                if (r.hasException()) {
                    exmsg = ", Exception: " + r.getConciseStackTrace();
                }
                LOGGER.error("Health Check Failure: " + r.getName() + ", Code: " + r.getHttpCode() + exmsg);

                return new ResponseEntity<String>("", HttpStatus.valueOf(code));
            }
        }
        // If we've made it this far, return OK.
        return okString;
    }

    /**
     * Tests to see if the heartbeat.txt file exists.  Uses skeletor.heartbeat_path property if available.
     *
     * @return True if it's been found.  False all other times, including all exceptions.
     */
    private boolean heartbeatFilePresent() {

        String webroot = servletContext.getRealPath("/");
        String heartbeatPath;

        if (webroot == null) {
            LOGGER.error("Cannot retrieve real server path.");
            return false;
        }

        if (skeletor.getHeartbeatPath() != null) {
            heartbeatPath = skeletor.getHeartbeatPath();
        } else {
            heartbeatPath = webroot + heartbeat;
        }

        File file = new File(heartbeatPath);
        return file.exists();
    }
}