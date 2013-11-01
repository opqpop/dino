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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * RESTful interface to display application status
 * User: dbreen / traack
 * Date: 8/28/13
 */
@Controller
public class StatusService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

    // Singleton Skeletor instance injected by Spring
    @Autowired
    private Skeletor skeletor;
    
    // Singleton HealthChecks instance injected by Spring
    @Autowired
    private HealthChecks healthChecks;

    /**
     * Method to run health checks and display healthCheckResults.  In production environment, tersely limits the displayed output.
     *
     * @return Java object that is serialized into JSON.
     */
    @RequestMapping(value = {"/status.json"}, method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public ResponseEntity<?> status() {

        List<HealthCheckResult> healthCheckResults = new ArrayList<HealthCheckResult>();
        
        // BEGIN -- health checks ----------------------------------------------

        // This is an example.  After you add your own, you can clobber this one.

        healthCheckResults.add(healthChecks.testAlwaysTrue());

        // healthCheckResults.add(HealthChecks.testAlwaysError());

        // END -- health checks ------------------------------------------------

        HttpStatus status = HttpStatus.OK;
        
        // iterate over all health checks
        for (HealthCheckResult r : healthCheckResults) {
            if (r.fail()) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                String exMsg = "";
                if (r.hasException()) {
                    exMsg = ", Exception: " + r.getConciseStackTrace();
                }
                LOGGER.error("Health Check Failure: " + r.getName() + ", Code: " + r.getHttpCode() + exMsg);

                // fail fast in production unless we're told to be verbose in skeletor.global.properties
                if (skeletor.isProduction() && skeletor.isTerseInProduction()) {
                    return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }

        if (skeletor.isProduction()) {
            if (skeletor.isVerboseInProduction()) {
                return new ResponseEntity<AppStatus>(new AppStatus(healthCheckResults), status);
            }

            // Terse output (production default)
            AppStatus appStatus = new AppStatus();
            HashMap<String, String> h = new HashMap<String, String>();
            h.put("version", appStatus.getVersion());
            h.put("status", "OK");
            return new ResponseEntity<HashMap<String, String>>(h, status);
        }

        // if not in production, return verbose status.
        return new ResponseEntity<AppStatus>(new AppStatus(healthCheckResults), status);
    }
}
