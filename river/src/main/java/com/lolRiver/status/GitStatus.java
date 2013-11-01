package com.lolRiver.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class is injected with properties about the git repository state at build time. This information is supplied by
 * the maven plugin <b>pl.project13.maven.git-commit-id-plugin</b>
 * Exposed by Spring serializing its data and displaying in /git-status.json
 * <p/>
 * Spring creates and manages a singleton instance of this class. Do NOT instantiate this class yourself; use
 *
 * @Autowired to inject it into the classes that need it.
 * <p/>
 * User: dbreen / traack
 * Date: 8/28/13
 */
@Component
public class GitStatus {
    private String branch;
    private String describe;
    private String commitId;
    private String commitIdAbbrev;
    private String buildUserName;
    private String buildUserEmail;
    private String buildTime;
    private String commitUserName;
    private String commitUserEmail;
    private String commitMessageFull;
    private String commitMessageShort;
    private String commitTime;

    protected static final Logger LOGGER = LoggerFactory.getLogger(GitStatus.class);

    /**
     * Create the GitStatus object and load fields from git.properties file.
     * If the properties file cannot be found, set all the properties to "" (blank string)
     */
    public GitStatus() {
        try {
            Properties props = new Properties();
            URL gitPropsUrl = GitStatus.class.getResource("/git.properties");

            if (gitPropsUrl != null) {
                props.load(gitPropsUrl.openStream());

                this.commitIdAbbrev = props.get("git.commit.id.abbrev").toString();
                this.branch = props.get("git.branch").toString();
                this.describe = props.get("git.commit.id.describe").toString();
                this.commitId = props.get("git.commit.id").toString();
                this.buildUserName = props.get("git.build.user.name").toString();
                this.buildUserEmail = props.get("git.build.user.email").toString();
                this.buildTime = props.get("git.build.time").toString();
                this.commitUserName = props.get("git.commit.user.name").toString();
                this.commitUserEmail = props.get("git.commit.user.email").toString();
                this.commitMessageShort = props.get("git.commit.message.short").toString();
                this.commitMessageFull = props.get("git.commit.message.full").toString();
                this.commitTime = props.get("git.commit.time").toString();

            } else {
                LOGGER.error("Could not retrieve git.properties");
                setFieldsToBlank();
            }

        } catch (IOException e) {
            LOGGER.error("Exception: " + e.toString());
            setFieldsToBlank();
        }
    }

    /**
     * This method sets all the properties to a blank string "".  We don't want null values.
     */
    private void setFieldsToBlank() {
        LOGGER.error("Setting all values to ''");
        this.commitIdAbbrev = "";
        this.branch = "";
        this.describe = "";
        this.commitId = "";
        this.buildUserName = "";
        this.buildUserEmail = "";
        this.buildTime = "";
        this.commitUserName = "";
        this.commitUserEmail = "";
        this.commitMessageShort = "";
        this.commitMessageFull = "";
        this.commitTime = "";
    }


    /* Getters and Setters */

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getCommitIdAbbrev() {
        return commitIdAbbrev;
    }

    public void setCommitIdAbbrev(String commitIdAbbrev) {
        this.commitIdAbbrev = commitIdAbbrev;
    }

    public String getBuildUserName() {
        return buildUserName;
    }

    public void setBuildUserName(String buildUserName) {
        this.buildUserName = buildUserName;
    }

    public String getBuildUserEmail() {
        return buildUserEmail;
    }

    public void setBuildUserEmail(String buildUserEmail) {
        this.buildUserEmail = buildUserEmail;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getCommitUserName() {
        return commitUserName;
    }

    public void setCommitUserName(String commitUserName) {
        this.commitUserName = commitUserName;
    }

    public String getCommitUserEmail() {
        return commitUserEmail;
    }

    public void setCommitUserEmail(String commitUserEmail) {
        this.commitUserEmail = commitUserEmail;
    }

    public String getCommitMessageFull() {
        return commitMessageFull;
    }

    public void setCommitMessageFull(String commitMessageFull) {
        this.commitMessageFull = commitMessageFull;
    }

    public String getCommitMessageShort() {
        return commitMessageShort;
    }

    public void setCommitMessageShort(String commitMessageShort) {
        this.commitMessageShort = commitMessageShort;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("commitIdAbbrev", commitIdAbbrev);
        map.put("branch", branch);
        map.put("describe", describe);
        map.put("commitId", commitId);
        map.put("buildUserName", buildUserName);
        map.put("buildUserEmail", buildUserEmail);
        map.put("buildTime", buildTime);
        map.put("commitUserName", commitUserName);
        map.put("commitUserEmail", commitUserEmail);
        map.put("commitMessageShort", commitMessageShort);
        map.put("commitMessageFull", commitMessageFull);
        map.put("commitTime", commitTime);

        return map;
    }
}
