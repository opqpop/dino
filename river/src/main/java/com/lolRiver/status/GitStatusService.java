package com.lolRiver.status;

import com.lolRiver.Skeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: dbreen / traack
 * Date: 8/28/13
 */
@Controller
public class GitStatusService {

    // Singleton Skeletor instance injected by Spring
    @Autowired
    private Skeletor skeletor;

    // Singleton GitStatus instance injected by Spring
    @Autowired
    private GitStatus gitStatus;

    @RequestMapping(value = {"/git-status.json"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getGitStatus() {

        if (skeletor.isProduction() && skeletor.isTerseInProduction()) {
            // I don't know why the HashMap needs to be instantiated before being returned, but it does.
            HashMap<String, String> h = new HashMap<String, String>();
            h.put("commitId", gitStatus.getCommitId());
            return h;
        }

        // verbose otherwise
        return gitStatus.asMap();
    }
}
