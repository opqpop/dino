package com.lolRiver.river.controllers.deserializers.rawDataDeserializers.kassadin;

import com.jayway.jsonpath.JsonPath;
import com.lolRiver.river.controllers.deserializers.rawDataDeserializers.GameDeserializer;
import com.lolRiver.river.models.Game;
import com.lolRiver.river.util.DateUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mxia (mxia@lolRiver.com)
 *         9/28/13
 */

@Component
public class KassadinGameDeserializer implements GameDeserializer {
    private static final Logger LOGGER = Logger.getLogger(KassadinGameDeserializer.class.getName());

    private final String RECENT_GAMES_PATH = "$.data.gameStatistics.array";
    private final String STATISTICS_PATH = "statistics.array";
    private final String END_TIME_PATH = "createDate";
    private final String GAME_ID_PATH = "gameId";

    private String gamePath(int gameIndex) {
        return RECENT_GAMES_PATH + "[" + gameIndex + "]";
    }

    private boolean wonGame(int gameIndex, String json) {
        List<Map> statistics = JsonPath.read(json, gamePath(gameIndex) + "." + STATISTICS_PATH);
        for (Map statistic : statistics) {
            if (statistic.get("statType").equals("WIN")) {
                return true;
            }
        }
        return false;
    }

    private int gameId(int gameIndex, String json) {
        return JsonPath.read(json, gamePath(gameIndex) + "." + GAME_ID_PATH);
    }

    private Timestamp endTime(int gameIndex, String json) throws Exception {
        String startTime = JsonPath.read(json, gamePath(gameIndex) + "." + END_TIME_PATH);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy h:mm:ss a zzz");
        Date date = sdf.parse(startTime);
        Timestamp timestamp = DateUtil.dateTimeToTimestamp(new DateTime(date));

        // kassadin returns PST time, our DB should store all times in UTC
        return DateUtil.addHoursToTimestamp(7, timestamp);
    }

    @Override
    public List<Game> gamesFromJson(String json) throws Exception {
        List<Game> games = new ArrayList<Game>();
        List gamesFromJson = JsonPath.read(json, RECENT_GAMES_PATH);
        for (int i = 0; i < gamesFromJson.size(); i++) {
            Game game = new Game();
            game.setId(gameId(i, json));
            game.setWon(wonGame(i, json));
            game.setEndTime(endTime(i, json));
            games.add(game);
        }
        return games;
    }
}
