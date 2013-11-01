package com.lolRiver.river.controllers.fetchers.rawDataFetchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolRiver.config.ConfigMap;
import com.lolRiver.river.controllers.deserializers.rawDataDeserializers.kassadin.KassadinEloDeserializer;
import com.lolRiver.river.controllers.deserializers.rawDataDeserializers.kassadin.KassadinGameDeserializer;
import com.lolRiver.river.controllers.deserializers.rawDataDeserializers.kassadin.KassadinSummonerIdDeserializer;
import com.lolRiver.river.models.Elo;
import com.lolRiver.river.models.Game;
import com.lolRiver.river.models.LolUser;
import com.lolRiver.river.persistence.interfaces.EloDao;
import com.lolRiver.river.persistence.interfaces.GameDao;
import com.lolRiver.river.util.DateUtil;
import com.lolRiver.river.util.WebDataFetcher;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.security.internal.spec.TlsPrfParameterSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

@Component
public class KassadinRawDataFetcher implements RawDataFetcher {
    private static final Logger LOGGER = Logger.getLogger(KassadinRawDataFetcher.class.getName());

    private ConfigMap configMap;
    private ConfigMap kassadinConfigMap;
    private final String HEADER_API_KEY_NAME;
    private final String HEADER_API_KEY_VALUE;
    private final String RECENT_GAMES_FROM_REGION_USERNAME_URL;
    private final String ELO_FROM_REGION_USERNAME_URL;
    private final String SUMMONER_ID_FROM_REGION_USERNAME_URL;

    @Autowired
    private WebDataFetcher webDataFetcher;

    @Autowired
    private KassadinGameDeserializer gameDeserializer;

    @Autowired
    private KassadinEloDeserializer eloDeserializer;

    @Autowired
    private KassadinSummonerIdDeserializer summonerIdDeserializer;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private EloDao eloDao;

    private KassadinRawDataFetcher() {
        /* Note: this will throw a runtime exception if MERCHANTDATA_ENV system env variable
        * or merchantdata.environment java property isn't set.
        *
        * pom.xml (under <!-- Jetty Plugin -->) has been configured to set merchantdata.environment when you run mvn jetty:run
        * Just modify that to change your environment.
        *
        * If you want to set the system variable for some reason,
        * you must force mvn to see bash variables by putting
        *          setenv M2_HOME /usr/bin/mvn
        * inside /etc/launchd.conf
        */
        configMap = new ConfigMap();
        kassadinConfigMap = configMap.getConfigMap("kassadin");
        HEADER_API_KEY_NAME = kassadinConfigMap.getString("header_api_key_name");
        HEADER_API_KEY_VALUE = kassadinConfigMap.getString("header_api_key_value");
        RECENT_GAMES_FROM_REGION_USERNAME_URL = kassadinConfigMap.getString("recent_games_from_region_username_url");
        ELO_FROM_REGION_USERNAME_URL = kassadinConfigMap.getString("elo_from_region_username_url");
        SUMMONER_ID_FROM_REGION_USERNAME_URL = kassadinConfigMap.getString("summoner_id_from_region_username_url");
    }

    @Override
    public void fetchAndStoreRawData(List<LolUser> lolUsers) {
        for (LolUser lolUser : lolUsers) {
            fetchAndStoreRawData(lolUser);
        }
    }

    private void fetchAndStoreRawData(LolUser lolUser) {
        try {
            fetchAndStoreGames(lolUser);
        } catch (Exception e) {
            LOGGER.error("Couldn't fetch game raw data for user: " + lolUser, e);
        }

        try {
            fetchAndStoreElo(lolUser);
        } catch (Exception e) {
            LOGGER.error("Couldn't fetch elo raw data for user: " + lolUser, e);
        }
    }

    private void debugJson(String json) {
        try {
            Map map = new ObjectMapper().readValue(json, new TypeReference<Map>() {});
            System.out.println("");
        } catch (Exception e) {

        }
    }

    private void fetchAndStoreGames(LolUser lolUser) throws Exception {
        String region = lolUser.getRegion();
        String username = lolUser.getUsername().replace(" ", "%20");
        String url = String.format(RECENT_GAMES_FROM_REGION_USERNAME_URL, region, username);
        LOGGER.info("Fetching game data from url: " + url);
        String json = sendGet(url);
        debugJson(json);
        List<Game> games = null;
        try {
            games = gameDeserializer.gamesFromJson(json);
        } catch (Exception e) {
            LOGGER.error("Couldn't deserialize for user: " + lolUser + " url: " + url + " from json: " + json, e);
        }

        if (games == null || games.isEmpty()) {
            LOGGER.error("Couldn't find games for user: " + lolUser + " url: " + url +
                         "json: " + json);
        }
        for (Game game : games) {
            List<String> invalidFields = invalidFieldsForGame(game);
            if (invalidFields.isEmpty()) {
                boolean updated = gameDao.updateGame(game);
                if (updated) {
                    Game updatedGame = gameDao.getGameFromId(game.getId());
                    if (updatedGame == null) {
                        // TODO xia enable this once I have a good sample. Right now it's giving me too much
                        // messages
                        //LOGGER.error("Found recent game not tracked by MainClient: " + game);
                    } else {
                        LOGGER.info("Found matching game to update end_time. GameId: " + updatedGame.getId());
                        updatedGame.setLength(DateUtil.secondsBetweenOrderedTimestamps(updatedGame.getStartTime(), updatedGame.getEndTime()));
                        if (updatedGame.invalidLength() == null) {
                            gameDao.updateGame(updatedGame);
                        } else {
                            LOGGER.error("Read invalid length trying to use game: " + updatedGame);
                        }
                    }
                } else {
                    LOGGER.error("Update SQL query failed on game: " + game);
                }
            } else {
                LOGGER.error("Read invalid game from json: " + json + " url: " + url + "lolUser: " + lolUser +
                             "invalidFields: " + invalidFields + " game: " + game);
            }
        }
    }

    private List<String> invalidFieldsForGame(Game game) {
        List<String> list = new ArrayList<String>();

        String invalidEndTime = game.invalidEndTime();
        if (invalidEndTime != null) {
            list.add(invalidEndTime);
        }

        return list;
    }

    private void fetchAndStoreElo(LolUser lolUser) throws Exception {
        String region = lolUser.getRegion();
        String username = lolUser.getUsername().replace(" ", "%20");
        String url = String.format(ELO_FROM_REGION_USERNAME_URL, region, username);
        String json = sendGet(url);

        Elo elo = eloDeserializer.eloFromJson(json);
        if (elo == null) {
            LOGGER.error("Couldn't find elo matching RANKED_SOLO_5X5 for user: " + lolUser + " url: " + url + "json: " + json);
            return;
        }

        List<String> invalidFields = elo.invalidFields();
        if (invalidFields.isEmpty()) {
            elo.setUserId(lolUser.getId());
            eloDao.insertElo(elo);
        } else {
            LOGGER.error("Read invalid elo from json: " + json + " url: " + url + "lolUser: " + lolUser +
                         "invalideFields: " + invalidFields + " elo: " + elo);
        }
    }

    public String sendGet(String url) throws Exception {
        return webDataFetcher.get(url, HEADER_API_KEY_NAME, HEADER_API_KEY_VALUE);
    }

    public int fetchSummonerId(String username, String region) throws Exception {
        String url = String.format(SUMMONER_ID_FROM_REGION_USERNAME_URL, region, username.replace(" ", "%20"));
        String json = sendGet(url);
        return summonerIdDeserializer.summonerIdFromJson(json);
    }
}
