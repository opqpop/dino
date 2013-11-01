package com.lolRiver.river.controllers.fetchers.rawDataFetchers;

import com.lolRiver.achimala.leaguelib.MainClient;
import com.lolRiver.achimala.leaguelib.connection.LeagueServer;
import com.lolRiver.river.controllers.converters.ConverterCollection;
import com.lolRiver.river.models.LolUser;
import com.lolRiver.river.persistence.DaoCollection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/5/13
 */

@Component
public class RiotRawDataFetcher implements RawDataFetcher {
    private static final Logger LOGGER = Logger.getLogger(RiotRawDataFetcher.class.getName());

    @Autowired
    DaoCollection daoCollection;

    @Autowired
    ConverterCollection converterCollection;

    @Override
    // TODO xia get rid of this, used by tests now only
    public void fetchAndStoreRawData(List<LolUser> lolUsers) {
        Map<LeagueServer, List<LolUser>> map = new HashMap<LeagueServer, List<LolUser>>();
        List<LolUser> naUsers = new ArrayList<LolUser>();
        List<LolUser> euwUsers = new ArrayList<LolUser>();
        List<LolUser> brUsers = new ArrayList<LolUser>();
        for (LolUser lolUser : lolUsers) {
            String region = lolUser.getRegion().toUpperCase();
            if (region.equals(LeagueServer.NORTH_AMERICA.getServerCode())) {
                naUsers.add(lolUser);
            } else if (region.equals(LeagueServer.EUROPE_WEST.getServerCode())) {
                euwUsers.add(lolUser);
            } else if (region.equals(LeagueServer.BRAZIL.getServerCode())) {
                brUsers.add(lolUser);
            }
        }
        map.put(LeagueServer.NORTH_AMERICA, naUsers);
        map.put(LeagueServer.EUROPE_WEST, euwUsers);
        map.put(LeagueServer.BRAZIL, brUsers);

        for (LeagueServer leagueServer : map.keySet()) {
            try {
                fetchAndStoreRawData(map.get(leagueServer), leagueServer);
            } catch (Exception e) {
                // ignore so that a server being down doesn't affect rest of server fetching
            }
        }
    }

    public void fetchAndStoreRawData(List<LolUser> lolUsers, LeagueServer leagueServer) throws Exception {
        if (lolUsers == null || lolUsers.isEmpty()) {
            return;
        }

        MainClient mainClient = new MainClient();
        mainClient.retrieve(leagueServer, lolUsers, daoCollection, converterCollection);
    }
}
