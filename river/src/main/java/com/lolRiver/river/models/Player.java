package com.lolRiver.river.models;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/28/13
 */

public class Player {
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();

    private int id;
    private String username;

    // game specific fields
    private Champion championPlayed;
    private SummonerSpell summonerSpell1;
    private SummonerSpell summonerSpell2;
    private int teamId;

    private static final String USERNAME_STRING = "username";
    private static final String CHAMPION_PLAYED_STRING = "champion_played";
    private static final String SUMMONER_SPELL1_STRING = "summoner_spell_1";
    private static final String SUMMONER_SPELL2_STRING = "summoner_spell_2";
    private static final String TEAM_ID_STRING = "team_id";

    public static final int BLUE_SIDE_ID = 100;
    public static final int RED_SIDE_ID = 200;

    public Player() {

    }

    public static String playerInfoFromPlayer(Player player) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put(USERNAME_STRING, player.getUsername() == null ? "" : player.getUsername());
            map.put(CHAMPION_PLAYED_STRING, player.championPlayed == null ? "" : player.championPlayed.getName().name());
            map.put(SUMMONER_SPELL1_STRING, player.summonerSpell1 == null ? "" : player.summonerSpell1.getName().name());
            map.put(SUMMONER_SPELL2_STRING, player.summonerSpell2 == null ? "" : player.summonerSpell2.getName().name());
            map.put(TEAM_ID_STRING, String.valueOf(player.getTeamId()));
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            LOGGER.error("Couldn't convert player: " + player + "to player info " + e, e);
            throw new RuntimeException(e);
        }
    }

    public static Player playerFromPlayerInfo(String playerInfo, String playerId) {
        Player player = new Player().setId(Integer.valueOf(playerId));
        try {
            Map<String, String> map = mapper.readValue(playerInfo, new TypeReference<Map<String, String>>() {});
            player.setUsername(map.get(USERNAME_STRING));
            player.setChampionPlayed(Champion.fromString(map.get(CHAMPION_PLAYED_STRING)));
            player.setSummonerSpell1(SummonerSpell.fromString(map.get(SUMMONER_SPELL1_STRING)));
            player.setSummonerSpell2(SummonerSpell.fromString(map.get(SUMMONER_SPELL2_STRING)));
            player.setTeamId(Integer.valueOf(map.get(TEAM_ID_STRING)));
        } catch (Exception e) {
            LOGGER.error("Couldn't convert player info: " + playerInfo, e);
        }
        return player;
    }

    public List<String> invalidFields() {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isBlank(username)) {
            list.add(USERNAME_STRING);
        }
        if (championPlayed == null || StringUtils.isBlank(championPlayed.getName().name())) {
            list.add(CHAMPION_PLAYED_STRING);
        }
        if (summonerSpell1 == null || StringUtils.isBlank(summonerSpell1.getName().name())) {
            list.add(SUMMONER_SPELL1_STRING);
        }
        if (summonerSpell2 == null || StringUtils.isBlank(summonerSpell2.getName().name())) {
            list.add(SUMMONER_SPELL2_STRING);
        }
        if (teamId != BLUE_SIDE_ID && teamId != RED_SIDE_ID) {
            list.add(TEAM_ID_STRING);
        }
        return list;
    }

    public boolean isBlueSide() {
        return teamId == BLUE_SIDE_ID;
    }

    public boolean isRedSide() {
        return teamId == RED_SIDE_ID;
    }

    public boolean hasSummonerSpell(SummonerSpell.Name name) {
        if (summonerSpell1 != null && summonerSpell1.getName() == name) {
            return true;
        }
        if (summonerSpell2 != null && summonerSpell2.getName() == name) {
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public Player setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Champion getChampionPlayed() {
        return championPlayed;
    }

    public void setChampionPlayed(Champion championPlayed) {
        this.championPlayed = championPlayed;
    }

    public SummonerSpell getSummonerSpell1() {
        return summonerSpell1;
    }

    public void setSummonerSpell1(SummonerSpell summonerSpell1) {
        this.summonerSpell1 = summonerSpell1;
    }

    public SummonerSpell getSummonerSpell2() {
        return summonerSpell2;
    }

    public void setSummonerSpell2(SummonerSpell summonerSpell2) {
        this.summonerSpell2 = summonerSpell2;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", championPlayed=" + championPlayed +
                ", summonerSpell1=" + summonerSpell1 +
                ", summonerSpell2=" + summonerSpell2 +
                ", teamId=" + teamId +
                '}';
    }
}
