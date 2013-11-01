package com.lolRiver.river.controllers.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolRiver.achimala.leaguelib.models.LeagueGame;
import com.lolRiver.achimala.leaguelib.models.LeagueSummoner;
import com.lolRiver.achimala.leaguelib.models.TeamType;
import com.lolRiver.river.models.Game;
import com.lolRiver.river.models.Player;
import org.apache.log4j.Logger;
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
public class LeagueGameToGameConverter {
    private static final Logger LOGGER = Logger.getLogger(LeagueGameToGameConverter.class.getName());
    private static ObjectMapper mapper = new ObjectMapper();

    public Game convert(LeagueGame leagueGame) {
        if (leagueGame == null) {
            return null;
        }
        Game game = new Game();
        game.setId(leagueGame.getId());
        if (leagueGame.getQueue() == null) {
            LOGGER.error("LeagueGame has null queue type: " + leagueGame);
        }
        game.setType(leagueGame.getQueue() == null ? "" : leagueGame.getQueue().name());
        game.setStartTime(leagueGame.getStartTime());
        initPlayers(game, leagueGame);

        List<String> invalidFields = invalidFieldsForGame(game);
        if (!invalidFields.isEmpty()) {
            LOGGER.error("invalid fields from converting leagueGame to game. invalidFields: " + invalidFields + " " +
                         "Game: " + game + " leagueGame: " + leagueGame);
            throw new RuntimeException();
        }
        return game;
    }

    private List<String> invalidFieldsForGame(Game game) {
        List<String> list = new ArrayList<String>();

        String invalidType = game.invalidTypeField();
        if (invalidType != null) {
            list.add(invalidType);
        }
        String invalidStartTime = game.invalidStartTime();
        if (invalidStartTime != null) {
            list.add(invalidStartTime);
        }
        list.addAll(game.invalidPlayerFields());
        return list;
    }

    private void initPlayers(Game game, LeagueGame leagueGame) {
        initPlayerIds(game, leagueGame);
        initPlayerInfos(game, leagueGame);
    }

    private void initPlayerIds(Game game, LeagueGame leagueGame) {
        List<LeagueSummoner> bluePlayers = leagueGame.getPlayerTeam();
        List<LeagueSummoner> redPlayers = leagueGame.getEnemyTeam();
        if (leagueGame.getPlayerTeamType() == TeamType.PURPLE) {
            List<LeagueSummoner> temp = bluePlayers;
            bluePlayers = redPlayers;
            redPlayers = temp;
        }

        for (int i = 0; i < bluePlayers.size(); i++) {
            LeagueSummoner summoner = bluePlayers.get(i);
            switch (i) {
                case 0:
                    game.setBluePlayer1Id(summoner.getId());
                case 1:
                    game.setBluePlayer2Id(summoner.getId());
                case 2:
                    game.setBluePlayer3Id(summoner.getId());
                case 3:
                    game.setBluePlayer4Id(summoner.getId());
                case 4:
                    game.setBluePlayer5Id(summoner.getId());
                default:
                    break;
            }
        }

        for (int i = 0; i < redPlayers.size(); i++) {
            LeagueSummoner summoner = redPlayers.get(i);
            switch (i) {
                case 0:
                    game.setRedPlayer1Id(summoner.getId());
                case 1:
                    game.setRedPlayer2Id(summoner.getId());
                case 2:
                    game.setRedPlayer3Id(summoner.getId());
                case 3:
                    game.setRedPlayer4Id(summoner.getId());
                case 4:
                    game.setRedPlayer5Id(summoner.getId());
                default:
                    break;
            }
        }
    }

    private void initPlayerInfos(Game game, LeagueGame leagueGame) {
        String error = "couldn't serialize playerInfo from leagueGame: " + leagueGame + "\n";

        try {
            Map<String, String> map = new HashMap<String, String>();
            Map<String, Player> champions = leagueGame.getPlayerChampionSelections();
            for (String playerId : champions.keySet()) {
                Player player = champions.get(playerId);
                String playerInfo = Player.playerInfoFromPlayer(player);
                map.put(playerId, playerInfo);
            }
            String playerInfo = mapper.writeValueAsString(map);
            if (playerInfo == null) {
                throw new RuntimeException(error);
            }
            game.setPlayersInfo(playerInfo);
        } catch (Exception e) {
            LOGGER.error(error, e);
        }
    }
}
