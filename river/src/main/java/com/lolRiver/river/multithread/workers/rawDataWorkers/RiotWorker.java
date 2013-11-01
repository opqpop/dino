package com.lolRiver.river.multithread.workers.rawDataWorkers;

import com.lolRiver.achimala.leaguelib.connection.LeagueServer;
import com.lolRiver.river.controllers.fetchers.rawDataFetchers.RiotRawDataFetcher;
import com.lolRiver.river.models.LolUser;
import com.lolRiver.river.multithread.workers.Worker;
import com.lolRiver.river.persistence.interfaces.LolUserDao;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mxia (mxia@lolRiver.com)
 *         10/5/13
 */

public class RiotWorker extends Worker {
    private static final Logger LOGGER = Logger.getLogger(RiotWorker.class.getName());

    private RiotRawDataFetcher rawDataFetcher;
    private LolUserDao lolUserDao;
    private LeagueServer leagueServer;

    public RiotWorker(RiotRawDataFetcher rawDataFetcher, LolUserDao lolUserDao, LeagueServer leagueServer) {
        if (rawDataFetcher == null) {
            throw new IllegalArgumentException("rawDataFetcher cannot be null when instantiating GameRawDataWorker");
        } else if (lolUserDao == null) {
            throw new IllegalArgumentException("lolUserDao cannot be null when instantiating GameRawDataWorker");
        } else if (leagueServer == null) {
            throw new IllegalArgumentException("leagueServer cannot be null when instantiating GameRawDataWorker");
        }
        this.rawDataFetcher = rawDataFetcher;
        this.lolUserDao = lolUserDao;
        this.leagueServer = leagueServer;
    }

    @Override
    public void run() {
        LOGGER.info("RiotWorker - run() started");

        List<LolUser> lolUsers = lolUserDao.getLolUsers();
        List<LolUser> list = new ArrayList<LolUser>();
        for (LolUser lolUser : lolUsers) {
            if (lolUser.getRegion().toUpperCase().equals(leagueServer.getServerCode())) {
                list.add(lolUser);
            }
        }
        try {
            rawDataFetcher.fetchAndStoreRawData(list, leagueServer);
        } catch (Exception e) {
            LOGGER.error("Couldn't get data for server: " + leagueServer);
        }

        LOGGER.info("RiotWorker - run() ended");
    }
}
