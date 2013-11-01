package com.lolRiver.river.multithread;

import com.lolRiver.config.ConfigMap;
import com.lolRiver.river.multithread.workerStarters.VideoToClipWorkerStarter;
import com.lolRiver.river.multithread.workerStarters.WorkerStarter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/2/13
 */

@Component
public class BackgroundWorkerSubmitter extends WorkerSubmitter {
    private static final Logger LOGGER = Logger.getLogger(BackgroundWorkerSubmitter.class.getName());

    // Add all WorkerStarters here
    @Autowired
    private VideoToClipWorkerStarter videoToClipWorkerStarter;

    @Override
    public void init(boolean startNow) {
        workerStarters = new ArrayList<WorkerStarter>();
        //workerStarters.add(videoToClipWorkerStarter);

        super.init(startNow);
    }

    @Override
    public ConfigMap configMap() {
        return new ConfigMap().getConfigMap("background_worker_submitter");
    }
}
