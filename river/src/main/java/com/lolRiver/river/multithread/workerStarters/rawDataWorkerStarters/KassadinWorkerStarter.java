package com.lolRiver.river.multithread.workerStarters.rawDataWorkerStarters;

import com.lolRiver.river.controllers.fetchers.rawDataFetchers.KassadinRawDataFetcher;
import com.lolRiver.river.multithread.workerStarters.ConstantIntervalWorkerStarter;
import com.lolRiver.river.multithread.workers.Worker;
import com.lolRiver.river.multithread.workers.rawDataWorkers.KassadinWorker;
import com.lolRiver.river.persistence.interfaces.LolUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

@Component
public class KassadinWorkerStarter extends ConstantIntervalWorkerStarter {
    public final int MAX_API_CALLS_PER_DAY = 1200; // calls / day
    private final int NUM_MINUTES_PER_DAY = 24 * 60;  // minutes / day
    // wait time before next call (using 20 min / game * 10 recent games)
    private int callIntervalMinutes = 200;  // minutes / call

    private int limitedCallIntervalMinutesForTesting = -1;   // used for testing only, clients should never set this

    public void setLimitedCallIntervalMinutesForTesting(int limitedCallIntervalMinutesForTesting) {
        this.limitedCallIntervalMinutesForTesting = limitedCallIntervalMinutesForTesting;
    }

    public void resetLimitedCallIntervalMinutesForTesting() {
        limitedCallIntervalMinutesForTesting = -1;
    }

    @Autowired
    private KassadinRawDataFetcher fetcher;

    @Autowired
    private LolUserDao lolUserDao;

    @Override
    public List<Worker> workersToStart() {
        List<Worker> workers = new ArrayList<Worker>();
        workers.add(new KassadinWorker(fetcher, lolUserDao));
        return workers;
    }

    public int numTrackedStreamerAccounts() {
        return 90;
    }

    public int limitedCallIntervalMinutes() {
        if (limitedCallIntervalMinutesForTesting != -1) {
            return limitedCallIntervalMinutesForTesting;
        }
        return Math.max(callIntervalMinutes, NUM_MINUTES_PER_DAY * numTrackedStreamerAccounts() / MAX_API_CALLS_PER_DAY + 1);
    }
}
