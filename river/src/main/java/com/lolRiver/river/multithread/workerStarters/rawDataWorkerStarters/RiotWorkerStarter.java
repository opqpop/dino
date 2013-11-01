package com.lolRiver.river.multithread.workerStarters.rawDataWorkerStarters;

import com.lolRiver.achimala.leaguelib.connection.LeagueServer;
import com.lolRiver.river.controllers.fetchers.rawDataFetchers.RiotRawDataFetcher;
import com.lolRiver.river.multithread.workerStarters.ConstantIntervalWorkerStarter;
import com.lolRiver.river.multithread.workers.Worker;
import com.lolRiver.river.multithread.workers.rawDataWorkers.RiotWorker;
import com.lolRiver.river.persistence.interfaces.LolUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/5/13
 */

@Component
public class RiotWorkerStarter extends ConstantIntervalWorkerStarter {
    private static final int CALL_INTERVAL_MINUTES = 10;  // minutes / call

    @Autowired
    private RiotRawDataFetcher fetcher;

    @Autowired
    private LolUserDao lolUserDao;

    @Override
    public List<Worker> workersToStart() {
        List<Worker> workers = new ArrayList<Worker>();
        workers.add(new RiotWorker(fetcher, lolUserDao, LeagueServer.NORTH_AMERICA));
        workers.add(new RiotWorker(fetcher, lolUserDao, LeagueServer.EUROPE_WEST));
        workers.add(new RiotWorker(fetcher, lolUserDao, LeagueServer.BRAZIL));
        return workers;
    }

    public int limitedCallIntervalMinutes() {
        return CALL_INTERVAL_MINUTES;
    }
}
