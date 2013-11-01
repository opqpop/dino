/*
 *  This file is part of LeagueLib.
 *  LeagueLib is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  LeagueLib is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with LeagueLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.lolRiver.achimala.leaguelib.models;

import com.lolRiver.river.models.Champion;
import com.lolRiver.river.models.Player;
import com.lolRiver.river.models.SummonerSpell;
import com.lolRiver.river.util.DateUtil;
import com.lolRiver.rtmp.TypedObject;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.*;

public class LeagueGame implements PlayerList {
    private static final Logger LOGGER = Logger.getLogger(LeagueGame.class.getName());

    private String _gameType, _gameMode;
    private LeagueMatchmakingQueue _queue = null;
    private int _id = -1;
    private List<LeagueSummoner> _playerTeam, _enemyTeam;
    private TeamType _playerTeamType = TeamType.BLUE;
    private TeamType _enemyTeamType = TeamType.PURPLE;
    private Map<String, Player> _playerChampionSelections;
    private Map<TeamType, List<LeagueChampion>> _bannedChampions;
    private Timestamp _startTime;

    private void fillListWithPlayers(List<LeagueSummoner> list, Object[] team, LeagueSummoner primarySummoner) throws
    Exception {
        for (Object o : team) {
            TypedObject to = (TypedObject)o;
            LeagueSummoner sum = new LeagueSummoner(to, primarySummoner.getServer(), true);
            if (sum.isEqual(primarySummoner)) {
                sum = primarySummoner;
            }
            list.add(sum);
        }
    }

    private Timestamp startTime(TypedObject obj) throws Exception {
        try {
            Object[] array = obj.getArray("teamOne");
            if (array == null || array.length == 0) {
                LOGGER.error("teamOne key was empty in obj so couldn't get startTime: " + obj);
            } else {
                for (Object teamOne : obj.getArray("teamOne")) {
                    TypedObject to = (TypedObject)teamOne;
                    Long timeAddedtoQueue = to.getLong("timeAddedToQueue");
                    if (timeAddedtoQueue == null) {
                        return DateUtil.getCurrentTimestamp();
                    }
                    Timestamp endTimePDT = DateUtil.timestampFromMilliseconds(timeAddedtoQueue);
                    // convert to UTC when storing into MYSQL
                    return DateUtil.addHoursToTimestamp(7, endTimePDT);
                }
            }
        } catch (Exception e) {
            LOGGER.error("error trying to get start time: " + obj, e);
            throw e;
        }
        return null;
    }

    public LeagueGame(TypedObject obj, LeagueSummoner primarySummoner) {
        try {
            obj = obj.getTO("game");
            _id = obj.getInt("id");
            _gameType = obj.getString("gameType");
            _gameMode = obj.getString("gameMode");
            _queue = LeagueMatchmakingQueue.valueOf(obj.getString("queueTypeName"));
            _startTime = startTime(obj);
            _playerTeam = new ArrayList<LeagueSummoner>();
            _enemyTeam = new ArrayList<LeagueSummoner>();
            fillListWithPlayers(_playerTeam, obj.getArray("teamOne"), primarySummoner);
            fillListWithPlayers(_enemyTeam, obj.getArray("teamTwo"), primarySummoner);
            if (!_playerTeam.contains(primarySummoner)) {
                swapTeams();
            }

            Map<String, String> playerTeamInternalNameToName = new HashMap<String, String>();
            Map<String, String> enemyTeamSummonerInternalNames = new HashMap<String, String>();
            for (LeagueSummoner leagueSummoner : _playerTeam) {
                playerTeamInternalNameToName.put(leagueSummoner.getInternalName(), leagueSummoner.getName());
            }
            for (LeagueSummoner leagueSummoner : _enemyTeam) {
                enemyTeamSummonerInternalNames.put(leagueSummoner.getInternalName(), leagueSummoner.getName());
            }
            _playerChampionSelections = new HashMap<String, Player>();
            for (Object o : obj.getArray("playerChampionSelections")) {
                TypedObject to = (TypedObject)o;

                Player player = new Player();
                String summonerInternalName = to.getString("summonerInternalName");
                if (playerTeamInternalNameToName.containsKey(summonerInternalName)) {
                    player.setUsername(playerTeamInternalNameToName.get(summonerInternalName));
                    player.setTeamId(_playerTeamType.getTeamId());
                } else if (enemyTeamSummonerInternalNames.containsKey(summonerInternalName)) {
                    player.setUsername(enemyTeamSummonerInternalNames.get(summonerInternalName));
                    player.setTeamId(300 - _playerTeamType.getTeamId());
                } else {
                    LOGGER.error("summonerInternalName: " + summonerInternalName + " not found in teamOne or teamTwo:" +
                                 " OBJECT: " + o + " OBJECTS: " + obj);
                }

                player.setChampionPlayed(Champion.fromId(String.valueOf(to.getInt("championId"))));
                // don't use getString, because they put decimals in spellId
                player.setSummonerSpell1(SummonerSpell.fromId(String.valueOf(to.getInt("spell1Id"))));
                player.setSummonerSpell2(SummonerSpell.fromId(String.valueOf(to.getInt("spell2Id"))));

                List<String> playerFieldErrors = player.invalidFields();
                if (!playerFieldErrors.isEmpty()) {
                    LOGGER.error("Invalid player fields: " + playerFieldErrors + " PLAYER: " + player + " OBJECT: " + o + " OBJECTS: " + obj);
                }
                String userId = summonerInternalName;
                boolean foundUserId = false;
                for (LeagueSummoner summoner : getAllPlayers()) {
                    if (summoner.getInternalName().equals(summonerInternalName)) {
                        userId = String.valueOf(summoner.getId());
                        foundUserId = true;
                        break;
                    }
                }
                if (!foundUserId) {
                    LOGGER.error("Didn't find userId for internalUsername: " + summonerInternalName + " OBJECT: " + o + " OBJECTS: " + obj);
                }
                _playerChampionSelections.put(userId, player);
            }
            _bannedChampions = new HashMap<TeamType, List<LeagueChampion>>();
            for (TeamType t : TeamType.values()) {
                _bannedChampions.put(t, new ArrayList<LeagueChampion>());
            }
            Object[] sortedBans = obj.getArray("bannedChampions");
            Arrays.sort(sortedBans, new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    TypedObject to1 = (TypedObject)o1;
                    TypedObject to2 = (TypedObject)o2;
                    return to1.getInt("pickTurn").compareTo(to2.getInt("pickTurn"));
                }
            });
            for (Object o : sortedBans) {
                TypedObject to = (TypedObject)o;
                TeamType teamType = TeamType.getFromId(to.getInt("teamId"));
                LeagueChampion champion = LeagueChampion.getChampionWithId(to.getInt("championId"));
                _bannedChampions.get(teamType).add(champion);
            }
        } catch (Exception e) {
            LOGGER.error("Error trying to create league game: " + obj);
        }
    }

    public void setGameType(String type) {
        _gameType = type;
    }

    public void setGameMode(String mode) {
        _gameMode = mode;
    }

    public void setId(int id) {
        _id = id;
    }

    public void setQueue(LeagueMatchmakingQueue queue) {
        _queue = queue;
    }

    public void swapTeams() {
        List<LeagueSummoner> temp = _playerTeam;
        _playerTeam = _enemyTeam;
        _enemyTeam = temp;
        _playerTeamType = TeamType.PURPLE;
        _enemyTeamType = TeamType.BLUE;
    }

    public String getGameType() {
        return _gameType;
    }

    public String getGameMode() {
        return _gameMode;
    }

    public int getId() {
        return _id;
    }

    public LeagueMatchmakingQueue getQueue() {
        return _queue;
    }

    public Timestamp getStartTime() {
        return _startTime;
    }

    public List<LeagueSummoner> getPlayerTeam() {
        return _playerTeam;
    }

    public List<LeagueSummoner> getEnemyTeam() {
        return _enemyTeam;
    }

    public List<LeagueSummoner> getAllPlayers() {
        try {
            List<LeagueSummoner> players = new ArrayList<LeagueSummoner>(_playerTeam);
            players.addAll(_enemyTeam);
            return players;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Map<String, Player> getPlayerChampionSelections() {
        return _playerChampionSelections;
    }

    public TeamType getPlayerTeamType() {
        return _playerTeamType;
    }

    public TeamType getEnemyTeamType() {
        return _enemyTeamType;
    }

    public List<LeagueChampion> getBannedChampionsForTeam(TeamType type) {
        return _bannedChampions.get(type);
    }

    @Override
    public String toString() {
        return "LeagueGame{" +
               "_gameType='" + _gameType + '\'' +
               ", _gameMode='" + _gameMode + '\'' +
               ", _queue=" + _queue +
               ", _playerTeam=" + _playerTeam +
               ", _enemyTeam=" + _enemyTeam +
               ", _playerTeamType=" + _playerTeamType +
               ", _enemyTeamType=" + _enemyTeamType +
               ", _playerChampionSelections=" + _playerChampionSelections +
               ", _startTime=" + _startTime +
               ", _id=" + _id +
               '}';
    }
}