package com.lolRiver.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

/**
 * Class to house result of an individual health check.  If the health check passes, httpCode should be set to 200.
 * User: dbreen
 * Date: 5/1/13
 * Time: 2:17 PM
 */

public class HealthCheckResult {

    private String name;
    private int httpCode;
    private Exception exception = null;
    private boolean pass;

    // --------------------------------------------------------------------
    // Constructors

    public HealthCheckResult(String name, int httpCode, Exception exception) {
        this(name, httpCode);
        this.exception = exception;
    }

    public HealthCheckResult(String name, int httpCode) {
        this.name = name;
        this.httpCode = httpCode;
        this.pass = (httpCode == HttpStatus.OK.value());
    }

    // --------------------------------------------------------------------
    // Methods

    public boolean hasException() {
        return (exception != null);
    }

    // --------------------------------------------------------------------
    // Getters & Setters

    public boolean pass() {
        return pass;
    }

    public boolean fail() {
        return !pass;
    }

    public String getName() {
        return name;
    }

    public int getHttpCode() {
        return httpCode;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getConciseStackTrace() {
        if (exception == null) {
            return null;
        }
        return exception.toString();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getFullStackTrace() {
        if (exception == null) {
            return null;
        }
        return ExceptionUtils.getStackTrace(exception);
    }

}