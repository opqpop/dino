package com.lolRiver.river.multithread.workers.rawDataWorkers;

import com.lolRiver.river.controllers.fetchers.rawDataFetchers.RawDataFetcher;
import com.lolRiver.river.multithread.workers.Worker;
import com.lolRiver.river.persistence.interfaces.LolUserDao;
import org.apache.log4j.Logger;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

public class KassadinWorker extends Worker {
    private static final Logger LOGGER = Logger.getLogger(KassadinWorker.class.getName());

    private RawDataFetcher rawDataFetcher;
    private LolUserDao lolUserDao;

    public KassadinWorker(RawDataFetcher rawDataFetcher, LolUserDao lolUserDao) {
        if (rawDataFetcher == null) {
            throw new IllegalArgumentException("rawDataFetcher cannot be null when instantiating GameRawDataWorker");
        } else if (lolUserDao == null) {
            throw new IllegalArgumentException("lolUserDao cannot be null when instantiating GameRawDataWorker");
        }
        this.rawDataFetcher = rawDataFetcher;
        this.lolUserDao = lolUserDao;
    }

    @Override
    public void run() {
        LOGGER.info("KassadinWorker - run() started");

        rawDataFetcher.fetchAndStoreRawData(lolUserDao.getLolUsers());

        LOGGER.info("KassadinWorker - run() ended");
    }
}
