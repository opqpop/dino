package com.lolRiver.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * This class should house all of your health checks (db, api, etc).  An example is provided.  Your tests should
 * return a HealthCheckResult object.  If your elophant passes, return code 200.  You are free to choose what HTTP status
 * code gets returned if your elophant(s) fail, as long as it's not 200.
 * See http://en.wikipedia.org/wiki/List_of_HTTP_status_codes for HTTP status codes.
 * 
 * Spring creates and manages a singleton instance of this class. Do NOT instantiate this class yourself; use 
 * @Autowired to inject it into the classes that need it.
 *
 * User: dbreen / traack
 * Date: 8/28/13
 */
@Component
public class HealthChecks {

    protected static final Logger LOGGER = LoggerFactory.getLogger(HealthChecks.class);

    /**
     * Example of a custom health check that always returns true.  Feel free to clobber.
     * @return HealthCheckResult object containing the result of running this health check.
     */
    public HealthCheckResult testAlwaysTrue() {
        return new HealthCheckResult("testAlwaysTrue", HttpStatus.OK.value());
    }

    /**
     * Example of a custom health check that always returns 500.  Feel free to clobber.
     * @return HealthCheckResult object containing the result of running this health check.
     */
    public HealthCheckResult testAlwaysError() {
        return new HealthCheckResult("testAlwaysError", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
