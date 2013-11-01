package com.lolRiver.river.multithread;

/**
 * @author mxia (mxia@lolRiver.com)
 *         10/2/13
 */

import com.lolRiver.config.ConfigMap;
import com.lolRiver.river.multithread.workerStarters.WorkerStarter;
import com.lolRiver.river.multithread.workerStarters.rawDataWorkerStarters.KassadinWorkerStarter;
import com.lolRiver.river.multithread.workerStarters.rawDataWorkerStarters.RiotWorkerStarter;
import com.lolRiver.river.multithread.workerStarters.rawDataWorkerStarters.TwitchWorkerStarter;
import com.lolRiver.river.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author mxia (mxia@lolRiver.coma)
 *         9/29/13
 */

@Component
public class RawDataWorkerSubmitter extends WorkerSubmitter {
    private static final Logger LOGGER = Logger.getLogger(RawDataWorkerSubmitter.class.getName());

    // Add all WorkerStarters here
    @Autowired
    private RiotWorkerStarter riotWorkerStarter;

    @Autowired
    private KassadinWorkerStarter kassadinWorkerStarter;

    @Autowired
    private TwitchWorkerStarter twitchWorkerStarter;

    @Override
    public void init(boolean startNow) {
        Timestamp now = DateUtil.getCurrentTimestamp();
        workerStarters = new ArrayList<WorkerStarter>();
        kassadinWorkerStarter.setLastStart(now);
        riotWorkerStarter.setLastStart(now);
        twitchWorkerStarter.setLastStart(now);
        workerStarters.add(kassadinWorkerStarter);
        workerStarters.add(riotWorkerStarter);
        //workerStarters.add(twitchWorkerStarter);

        super.init(startNow);
    }

    @Override
    public ConfigMap configMap() {
        return new ConfigMap().getConfigMap("raw_data_worker_submitter");
    }
}
