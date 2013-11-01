package com.lolRiver.river.controllers.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolRiver.achimala.leaguelib.models.LeagueSummoner;
import com.lolRiver.river.models.LolUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/5/13
 */

@Component
public class LeagueSummonerToLolUserConverter {
    private static final Logger LOGGER = Logger.getLogger(LeagueSummonerToLolUserConverter.class.getName());
    private static ObjectMapper mapper = new ObjectMapper();

    public LolUser convert(LeagueSummoner leagueSummoner) {
        if (leagueSummoner == null) {
            return null;
        }
        LolUser lolUser = new LolUser();
        lolUser.setId(leagueSummoner.getId());
        lolUser.setUserName(leagueSummoner.getName());
        if (leagueSummoner.getServer() == null) {
            LOGGER.error("LeagueSummoner has null region: " + leagueSummoner);
        }
        lolUser.setRegion(leagueSummoner.getServer() == null ? "" : leagueSummoner.getServer().name());

        List<String> invalidFields = invalidFieldsForLolUser(lolUser);
        if (!invalidFields.isEmpty()) {
            LOGGER.error("invalid fields from converting leagueSummoner to lolUser. invalidFields: " + invalidFields + " " +
                         "LolUser: " + lolUser + " leagueSummoner: " + leagueSummoner);
            throw new RuntimeException();
        }
        return lolUser;
    }

    private List<String> invalidFieldsForLolUser(LolUser lolUser) {
        List<String> list = new ArrayList<String>();

        String invalidId = lolUser.invalidIdField();
        if (invalidId != null) {
            list.add(invalidId);
        }
        String invalidUsername = lolUser.invalidUsername();
        if (invalidUsername != null) {
            list.add(invalidUsername);
        }
        String invalidRegion = lolUser.invalidRegion();
        if (invalidRegion != null) {
            list.add(invalidRegion);
        }
        String invalidStreamerName = lolUser.invalidStreamerName();
        if (invalidStreamerName != null) {
            list.add(invalidStreamerName);
        }
        return list;
    }
}
