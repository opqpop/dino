package com.lolRiver.status;

import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

/**
 * This class represents the current status of this application.
 * Exposed by serializing it and displaying in /status.json
 */
public class AppStatus {

    private Date time;
    private Date bootedAt;
    private String revision;
    private int httpCode;
    private List<HealthCheckResult> healthchecks;

    /**
     * Very basic constructor.
     */
    public AppStatus() {}

    /**
     * Constructor
     *
     * @param healthchecks A list of HealthCheckResult objects.
     */
    public AppStatus(List<HealthCheckResult> healthchecks) {
        GitStatus git = new GitStatus();
        this.revision = git.getCommitId();
        this.time = new Date();
        this.healthchecks = healthchecks;
    }

    /**
     * Get overall status of the App.  Returns 'Failure' if any of the healthchecks fail.
     *
     * @return A String containing 'Failure' or 'OK'
     */
    public String getStatus() {
        for (HealthCheckResult r : healthchecks) {
            if (r.getHttpCode() != HttpStatus.OK.value()) {
                return "Failure";
            }
        }
        return "OK";
    }

    /**
     * Provides the version of the application as created by the maven build via pom.xml.
     * Takes the form of ${project.version}-${buildNumber}
     *
     * @return String containing the current build version.
     */
    public String getVersion() {
        try {
            URL versionUrl = AppStatus.class.getResource("/version.properties");
            if (versionUrl == null) {
                return "version.properties not found";
            }
            Properties props = new Properties();
            props.load(versionUrl.openStream());

            return (String)props.get("skeletor.version");
        } catch (IOException e) {
            return "error opening version.properties: " + e.toString();
        }
    }

    public String getEnv() {
        return System.getProperty("merchantdata.environment");
    }

    public String getRevision() {
        return revision;
    }

    /**
     * Returns the raw time. Used for convenience of testing.
     *
     * @return The time member variable in Date format.
     */
    public Date getRawTime() {
        return this.time;
    }

    /**
     * Get the current time.
     *
     * @return String containing the formatted current time.
     */
    public String getTime() {
        return formatTime(time);
    }

    /*
     * Helper to standardize the date formatting for displayed dates.
     */
    public static String formatTime(Date rawTime) {
        return formatTime(rawTime.getTime());
    }

    /*
     * Support long input parameter like SimpleDateFormat.format().
     */
    public static String formatTime(long timeEpoch) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(timeEpoch);
    }

    /**
     * Return the timestamp when the JVM was booted
     *
     * @return String containing the timestamp when the JVM was booted
     */
    public String getBootedAt() {
        Long jvmUpTime = ManagementFactory.getRuntimeMXBean().getUptime();
        Date now = new Date();
        // Docs says Date() constructor takes a long but this gives us a null object.
        Date uptime = new Date();
        uptime.setTime(now.getTime() - jvmUpTime);

        return formatTime(uptime);
    }

    public List<HealthCheckResult> getHealthChecks() {
        return healthchecks;
    }

}
