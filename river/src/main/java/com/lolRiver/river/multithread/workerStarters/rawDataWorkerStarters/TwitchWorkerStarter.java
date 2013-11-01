package com.lolRiver.river.multithread.workerStarters.rawDataWorkerStarters;

import com.lolRiver.river.controllers.fetchers.videoFetchers.TwitchVideoFetcher;
import com.lolRiver.river.multithread.workerStarters.ConstantIntervalWorkerStarter;
import com.lolRiver.river.multithread.workers.Worker;
import com.lolRiver.river.multithread.workers.rawDataWorkers.TwitchWorker;
import com.lolRiver.river.persistence.interfaces.StreamerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/30/13
 */

@Component
public class TwitchWorkerStarter extends ConstantIntervalWorkerStarter {
    // wait time before next call (using 20 min / game * 10 recent games)
    private int callIntervalMinutes = 360;  // minutes / call

    private int limitedCallIntervalMinutesForTesting = -1;   // used for testing only, clients should never set this

    public void setLimitedCallIntervalMinutesForTesting(int limitedCallIntervalMinutesForTesting) {
        this.limitedCallIntervalMinutesForTesting = limitedCallIntervalMinutesForTesting;
    }

    public void resetLimitedCallIntervalMinutesForTesting() {
        limitedCallIntervalMinutesForTesting = -1;
    }

    @Autowired
    private TwitchVideoFetcher fetcher;

    @Autowired
    private StreamerDao streamerDao;

    @Override
    public List<Worker> workersToStart() {
        List<Worker> workers = new ArrayList<Worker>();
        workers.add(new TwitchWorker(fetcher, streamerDao));
        return workers;
    }

    public int limitedCallIntervalMinutes() {
        if (limitedCallIntervalMinutesForTesting != -1) {
            return limitedCallIntervalMinutesForTesting;
        }
        return callIntervalMinutes;
    }
}
