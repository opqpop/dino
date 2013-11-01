package com.lolRiver.river.models;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/28/13
 */

public class Game {
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    public static final String ID_STRING = "id";
    public static final String TYPE_STRING = "type";
    public static final String BLUE_PLAYER1_ID_STRING = "blue_player1_id";
    public static final String BLUE_PLAYER2_ID_STRING = "blue_player2_id";
    public static final String BLUE_PLAYER3_ID_STRING = "blue_player3_id";
    public static final String BLUE_PLAYER4_ID_STRING = "blue_player4_id";
    public static final String BLUE_PLAYER5_ID_STRING = "blue_player5_id";
    public static final String RED_PLAYER1_ID_STRING = "red_player1_id";
    public static final String RED_PLAYER2_ID_STRING = "red_player2_id";
    public static final String RED_PLAYER3_ID_STRING = "red_player3_id";
    public static final String RED_PLAYER4_ID_STRING = "red_player4_id";
    public static final String RED_PLAYER5_ID_STRING = "red_player5_id";
    public static final String PLAYERS_INFO_STRING = "players_info";
    public static final String WON_STRING = "won";
    public static final String START_TIME_STRING = "start_time";
    public static final String END_TIME_STRING = "end_time";
    public static final String LENGTH_STRING = "length";

    private int id;
    private String type;
    private int bluePlayer1Id;
    private int bluePlayer2Id;
    private int bluePlayer3Id;
    private int bluePlayer4Id;
    private int bluePlayer5Id;
    private int redPlayer1Id;
    private int redPlayer2Id;
    private int redPlayer3Id;
    private int redPlayer4Id;
    private int redPlayer5Id;
    private String playersInfo;
    private boolean won;
    private Timestamp startTime;
    private Timestamp endTime;
    private int length;

    private static ObjectMapper mapper = new ObjectMapper();
    // instance fields used for caching
    private List<Player> bluePlayers;
    private List<Player> redPlayers;
    private List<Player> allPlayers;

    public enum Type {
        NONE,
        RANKED_SOLO_5x5,
        RANKED_TEAM_5x5,
        RANKED_TEAM_3x3,
        ARAM_UNRANKED_5x5,
        NORMAL
    }

    public static Type gameTypeFromString(String s) {
        if (StringUtils.isBlank(s)) {
            return Type.NONE;
        }
        return Type.valueOf(s);
    }

    public boolean isViewable() {
        if (!validateEverything().isEmpty()) {
            LOGGER.error("Game not viewable due to invalid fields: " + validateEverything() + "GAME: " + this);
            return false;
        }

        if (type.equals(Type.RANKED_SOLO_5x5.name()) || type.equals(Type.RANKED_TEAM_5x5)) {
            return true;
        }
        if (type.equals(Type.NORMAL.name()) && getAllPlayers().size() == 10) {
            return true;
        }
        return false;
    }

    public boolean hasPlayerRoles() {
        if (type.equals(Type.ARAM_UNRANKED_5x5.name())) {
            return false;
        }
        return true;
    }

    public String getAllPlayerDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type + " ");
        sb.append("BLUE CHAMPIONS: [");
        for (Player bluePlayer : bluePlayers) {
            sb.append("(")
            .append(bluePlayer.getChampionPlayed().getName().name())
            .append(", ")
            .append(bluePlayer.getSummonerSpell1().getName().name())
            .append(", ")
            .append(bluePlayer.getSummonerSpell2().getName().name())
            .append("), ");
        }
        sb.append("]  ===== RED CHAMPIONS: [");
        for (Player redPlayer : redPlayers) {
            sb.append("(")
            .append(redPlayer.getChampionPlayed().getName().name())
            .append(", ")
            .append(redPlayer.getSummonerSpell1().getName().name())
            .append(", ")
            .append(redPlayer.getSummonerSpell2().getName().name())
            .append("), ");
        }
        return sb.toString();
    }

    public List<Player> getAllPlayers() {
        if (allPlayers == null) {
            allPlayers = new ArrayList<Player>();
            allPlayers.addAll(getBluePlayers());
            allPlayers.addAll(getRedPlayers());
        }
        return allPlayers;
    }

    public List<Player> getBluePlayers() {
        if (bluePlayers == null) {
            bluePlayers = new ArrayList<Player>();
            if (playersInfo != null) {
                try {
                    Map<String, String> map = mapper.readValue(playersInfo, new TypeReference<Map<String,
                                                                                                 String>>() {});
                    for (String playerId : map.keySet()) {
                        String playerInfo = map.get(playerId);
                        Player player = Player.playerFromPlayerInfo(playerInfo, playerId);
                        if (player.isBlueSide()) {
                            bluePlayers.add(player);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("couldn't read playersInfo: " + playersInfo + " for this game: " + this);
                }
            }
        }
        return bluePlayers;
    }

    public List<Player> getRedPlayers() {
        if (redPlayers == null) {
            redPlayers = new ArrayList<Player>();
            if (playersInfo != null) {
                try {
                    Map<String, String> map = mapper.readValue(playersInfo, new TypeReference<Map<String,
                                                                                                 String>>() {});
                    for (String playerId : map.keySet()) {
                        String playerInfo = map.get(playerId);
                        Player player = Player.playerFromPlayerInfo(playerInfo, playerId);
                        if (player.isRedSide()) {
                            redPlayers.add(player);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("couldn't read playersInfo: " + playersInfo + " for this game: " + this);
                }
            }
        }
        return redPlayers;
    }

    public List<String> validateEverything() {
        List<String> list = new ArrayList<String>();

        String error = invalidTypeField();
        if (error != null) {
            list.add(error);
        }
        error = invalidStartTime();
        if (error != null) {
            list.add(error);
        }
        error = invalidEndTime();
        if (error != null) {
            list.add(error);
        }
        error = invalidLength();
        if (error != null) {
            list.add(error);
        }
        list.addAll(invalidPlayerFields());
        return list;
    }

    public String invalidTypeField() {
        return StringUtils.isBlank(type) ? TYPE_STRING : null;
    }

    public String invalidStartTime() {
        return startTime == null ? START_TIME_STRING : null;
    }

    public String invalidEndTime() {
        return endTime == null ? END_TIME_STRING : null;
    }

    public String invalidLength() {
        return length <= 0 ? LENGTH_STRING : null;
    }

    public List<String> invalidPlayerFields() {
        List<String> list = new ArrayList<String>();
        if (bluePlayer1Id < 0) {
            list.add(BLUE_PLAYER1_ID_STRING);
        }
        if (bluePlayer2Id < 0) {
            list.add(BLUE_PLAYER2_ID_STRING);
        }
        if (bluePlayer3Id < 0) {
            list.add(BLUE_PLAYER3_ID_STRING);
        }
        if (bluePlayer4Id < 0) {
            list.add(BLUE_PLAYER4_ID_STRING);
        }
        if (bluePlayer5Id < 0) {
            list.add(BLUE_PLAYER5_ID_STRING);
        }
        if (redPlayer1Id < 0) {
            list.add(RED_PLAYER1_ID_STRING);
        }
        if (redPlayer2Id < 0) {
            list.add(RED_PLAYER2_ID_STRING);
        }
        if (redPlayer3Id < 0) {
            list.add(RED_PLAYER3_ID_STRING);
        }
        if (redPlayer4Id < 0) {
            list.add(RED_PLAYER4_ID_STRING);
        }
        if (redPlayer5Id < 0) {
            list.add(RED_PLAYER5_ID_STRING);
        }

        for (Player player : getAllPlayers()) {
            list.addAll(appendStringToPlayerInvalidFields(player.getUsername(), player.invalidFields()));
        }

        if (getAllPlayers().size() != 10 &&
            (type.equals(Type.RANKED_TEAM_5x5.name()) || type.equals(Type.RANKED_SOLO_5x5.name()))) {
            list.add("Bad playersInfo size for game type: " + type + " playersInfo: " + playersInfo);
        }
        return list;
    }

    private List<String> appendStringToPlayerInvalidFields(String s, List<String> playerInvalidFields) {
        List<String> list = new ArrayList<String>();
        for (String playerInvalidField : playerInvalidFields) {
            list.add(s + "_" + playerInvalidField);
        }
        return list;
    }

    // ==== standard getters/setters below ====

    public int getId() {
        return id;
    }

    public Game setId(int id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public Game setType(String type) {
        this.type = type;
        return this;
    }

    public int getBluePlayer1Id() {
        return bluePlayer1Id;
    }

    public Game setBluePlayer1Id(int bluePlayer1Id) {
        this.bluePlayer1Id = bluePlayer1Id;
        return this;
    }

    public int getBluePlayer2Id() {
        return bluePlayer2Id;
    }

    public Game setBluePlayer2Id(int bluePlayer2Id) {
        this.bluePlayer2Id = bluePlayer2Id;
        return this;
    }

    public int getBluePlayer3Id() {
        return bluePlayer3Id;
    }

    public Game setBluePlayer3Id(int bluePlayer3Id) {
        this.bluePlayer3Id = bluePlayer3Id;
        return this;
    }

    public int getBluePlayer4Id() {
        return bluePlayer4Id;
    }

    public Game setBluePlayer4Id(int bluePlayer4Id) {
        this.bluePlayer4Id = bluePlayer4Id;
        return this;
    }

    public int getBluePlayer5Id() {
        return bluePlayer5Id;
    }

    public Game setBluePlayer5Id(int bluePlayer5Id) {
        this.bluePlayer5Id = bluePlayer5Id;
        return this;
    }

    public int getRedPlayer1Id() {
        return redPlayer1Id;
    }

    public Game setRedPlayer1Id(int redPlayer1Id) {
        this.redPlayer1Id = redPlayer1Id;
        return this;
    }

    public int getRedPlayer2Id() {
        return redPlayer2Id;
    }

    public Game setRedPlayer2Id(int redPlayer2Id) {
        this.redPlayer2Id = redPlayer2Id;
        return this;
    }

    public int getRedPlayer3Id() {
        return redPlayer3Id;
    }

    public Game setRedPlayer3Id(int redPlayer3Id) {
        this.redPlayer3Id = redPlayer3Id;
        return this;
    }

    public int getRedPlayer4Id() {
        return redPlayer4Id;
    }

    public Game setRedPlayer4Id(int redPlayer4Id) {
        this.redPlayer4Id = redPlayer4Id;
        return this;
    }

    public int getRedPlayer5Id() {
        return redPlayer5Id;
    }

    public Game setRedPlayer5Id(int redPlayer5Id) {
        this.redPlayer5Id = redPlayer5Id;
        return this;
    }

    public String getPlayersInfo() {
        return playersInfo;
    }

    public Game setPlayersInfo(String playersInfo) {
        this.playersInfo = playersInfo;
        return this;
    }

    public boolean isWon() {
        return won;
    }

    public Game setWon(boolean won) {
        this.won = won;
        return this;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Game setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        return this;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Game setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        return this;
    }

    public int getLength() {
        return length;
    }

    public Game setLength(int length) {
        this.length = length;
        return this;
    }

    @Override
    public String toString() {
        return "Game{" +
               "id=" + id +
               ", type='" + type + '\'' +
               ", playersInfo='" + playersInfo + '\'' +
               ", won=" + won +
               ", startTime=" + startTime +
               ", endTime=" + endTime +
               ", length=" + length +
               '}';
    }
}