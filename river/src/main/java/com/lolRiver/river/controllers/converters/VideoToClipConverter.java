package com.lolRiver.river.controllers.converters;

import com.lolRiver.river.models.*;
import com.lolRiver.river.persistence.DaoCollection;
import com.lolRiver.river.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/2/13
 */

@Component
public class VideoToClipConverter {
    private static final Logger LOGGER = Logger.getLogger(VideoToClipConverter.class.getName());
    private static final int DELAY_CONSTANT = 111;  // delay constant needed to get exact moment of queue start

    @Autowired
    private DaoCollection daoCollection;

    public boolean canConvert(Video video) {
        Game latestGame = daoCollection.getGameDao().getLatestGame();
        return latestGame.getEndTime().after(video.getEndTime());
    }

    public List<Clip> convert(Video video) throws Exception {
        List<Clip> clips = new ArrayList<Clip>();

        List<Game> matchingGames = daoCollection.getGameDao().getGamesMatchingVideo(video);
        for (Game matchingGame : matchingGames) {
            Clip clip = new Clip().setGameType(Game.gameTypeFromString(matchingGame.getType()));
            clip.setViewable(matchingGame.isViewable());
            if (matchingGame.isViewable()) {
                clip.setGameId(matchingGame.getId())
                        .setVideoId(video.getId())
                        .setStreamerName(video.getStreamerName())
                        .setStartTime(matchingGame.getStartTime())
                        .setEndTime(matchingGame.getEndTime())
                        .setLength(matchingGame.getLength())
                        .setViews(video.getViews());

                String url = clipUrl(video, matchingGame);
                clip.setUrl(url);

                initGameSpecificInfo(clip, video, matchingGame);
            }
            clips.add(clip);
        }
        return clips;
    }

    private String clipUrl(Video video, Game game) {
        int secondsBeforeGameStarts = DateUtil.secondsBetweenUnorderedTimestamps(video.getStartTime(), game.getStartTime());
        if (secondsBeforeGameStarts < 0) {
            throw new RuntimeException("trying to convert video to clip using game with earlier start time");
        }
        secondsBeforeGameStarts += DELAY_CONSTANT;
        int minutes = secondsBeforeGameStarts / 60;
        int seconds = secondsBeforeGameStarts % 60;
        String timestampToAddToUrl = String.format("?t=%dm%ds", minutes, seconds);
        return video.getUrl() + timestampToAddToUrl;
    }

    private void initGameSpecificInfo(Clip clip, Video video, Game game) {
        Streamer streamer = daoCollection.getStreamerDao().getStreamerFromName(video.getStreamerName());
        List<LolUser> lolUsers = daoCollection.getLolUserDao().getLolUsersFromStreamer(streamer);
        Map<Player, Role> playerToRoleMap = guessRolesFromGame(game, video);

        Player playerMatchingStreamer = null;
        for (Player player : playerToRoleMap.keySet()) {
            for (LolUser lolUser : lolUsers) {
                if (lolUser.getId() == player.getId()) {
                    playerMatchingStreamer = player;
                    Role rolePlayed = playerToRoleMap.get(player);
                    clip.setChampionPlayed(player.getChampionPlayed());
                    clip.setRolePlayed(rolePlayed);

                    Elo elo = new Elo()
                            .setUserId(lolUser.getId())
                            .setTime(game.getStartTime());
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Elo.USER_ID_STRING, "=");
                    params.put(Elo.TIME_STRING, "<");
                    clip.setElo(daoCollection.getEloDao().getLatestEloFromElo(elo, params));

                    break;
                }
            }
        }
        if (playerMatchingStreamer == null) {
            LOGGER.error(String.format("Couldn't find any of streamer's usernames for game. usernames: %s, " +
                    "game: %s playerToRoleMap.keyset(): %s",
                    lolUsers, game, playerToRoleMap.keySet()));
            return;
        }

        Role partnerRole = clip.getRolePlayed().partnerRole();
        for (Player player : playerToRoleMap.keySet()) {
            if (player == playerMatchingStreamer) {
                continue;
            }

            Role role = playerToRoleMap.get(player);
            Role streamerRole = playerToRoleMap.get(playerMatchingStreamer);
            Champion champion = player.getChampionPlayed();
            if (role.equals(streamerRole)) {
                clip.setChampionFaced(champion);
            } else if (partnerRole != null && role.equals(partnerRole)) {
                if (player.getTeamId() == playerMatchingStreamer.getTeamId()) {
                    clip.setLanePartnerChampion(champion);
                } else {
                    clip.setEnemyLanePartnerChampion(champion);
                }
            }
        }
    }

    private Map<Player, Role> guessRolesFromGame(Game game, Video video) {
        List<Player> bluePlayers = game.getBluePlayers();
        List<Player> redPlayers = game.getRedPlayers();

        Map<Player, Role> map = new HashMap<Player, Role>();
        try {
            insertRolesFromPlayers(bluePlayers, map, game, video);
            insertRolesFromPlayers(redPlayers, map, game, video);
        } catch (Exception e) {
            LOGGER.error("Error guessing roles: " + e + game + "\n" + video + "\n");
        }
        return map;
    }

    private void matchPlayerWithRole(Player player, Role role, Map<Player, Role> map, List<Player> availablePlayers,
                                     List<Role> availableRoles) {
        map.put(player, role);
        availableRoles.remove(role);
        availablePlayers.remove(player);
    }

    private void insertRolesFromPlayers(List<Player> players, Map<Player, Role> map, Game game, Video video) {
        if (!game.hasPlayerRoles()) {
            for (Player player : players) {
                map.put(player, new Role(Role.Name.NONE));
            }
            return;
        }

        List<Player> availablePlayers = new ArrayList<Player>(players);

        List<Role> availableRoles = new ArrayList<Role>();
        availableRoles.add(new Role(Role.Name.TOP));
        availableRoles.add(new Role(Role.Name.MID));
        availableRoles.add(new Role(Role.Name.JUNG));
        availableRoles.add(new Role(Role.Name.SUPP));
        availableRoles.add(new Role(Role.Name.ADC));

        if (availablePlayers.size() != availableRoles.size()) {
            throw new RuntimeException("sizes for available players and available roles don't match initially: " + availablePlayers
                    + " roles: " + availableRoles);
        }
        // deduce easy roles to identify via summoner spells
        for (Player player : players) {
            if (player.hasSummonerSpell(SummonerSpell.Name.SMITE)) {
                matchPlayerWithRole(player, new Role(Role.Name.JUNG), map, availablePlayers, availableRoles);
            } else if (player.hasSummonerSpell(SummonerSpell.Name.EXHAUST)
                    && !player.getChampionPlayed().cannotPlayRole(new Role(Role.Name.SUPP))) {
                matchPlayerWithRole(player, new Role(Role.Name.SUPP), map, availablePlayers, availableRoles);
            }
        }

        while (true) {
            if (guessByRolePlayableByOneChampion(availablePlayers, availableRoles, map)) {
                continue;
            }
            if (guessByChampionCanOnlyPlayOneRemainingRole(availablePlayers, availableRoles, map)) {
                continue;
            }
            if (guessByRolePreferredByOneChampion(availablePlayers, availableRoles, map)) {
                continue;
            }
            if (guessByChampionCanOnlyPreferOneRemainingRole(availablePlayers, availableRoles, map)) {
                continue;
            }
            break;
        }

        if (!availableRoles.isEmpty()) {
            List<Champion> champions = new ArrayList<Champion>();
            for (Player availablePlayer : availablePlayers) {
                champions.add(availablePlayer.getChampionPlayed());
            }
            LOGGER.warn("forced to assign random roles videoId: " + video.getId() + "champion set: " + champions +
                    "roleset:" + availableRoles + " Game: " + game.getAllPlayerDebugString());

            // randomly put roles in
            for (int i = 0; i < availableRoles.size(); i++) {
                map.put(availablePlayers.get(i), availableRoles.get(i));
            }
        }
    }

    private boolean guessByRolePreferredByOneChampion(List<Player> availablePlayers, List<Role> availableRoles,
                                                      Map<Player, Role> map) {
        int numAvailableRoles = availableRoles.size();

        int maxLoops = availableRoles.size();
        while (!availableRoles.isEmpty() && maxLoops > 0) {
            // If there's a role where only 1 out of remaining champions
            // prefer play it, match that
            for (int i = 0; i < availableRoles.size(); i++) {
                Role availableRole = availableRoles.get(i);
                Player potentialPlayer = null;
                for (Player availablePlayer : availablePlayers) {
                    Champion champion = availablePlayer.getChampionPlayed();
                    if (champion.shouldPlayRole(availableRole)) {
                        if (potentialPlayer == null) {
                            potentialPlayer = availablePlayer;
                        } else {
                            potentialPlayer = null;
                            break;
                        }
                    }
                }
                if (potentialPlayer != null) {
                    map.put(potentialPlayer, availableRole);
                    availablePlayers.remove(potentialPlayer);
                    availableRoles.remove(i--);
                }
            }
            maxLoops--;
        }

        return availableRoles.size() != numAvailableRoles;
    }

    private boolean guessByRolePlayableByOneChampion(List<Player> availablePlayers, List<Role> availableRoles,
                                                     Map<Player, Role> map) {
        int numAvailableRoles = availableRoles.size();

        int maxLoops = availableRoles.size();
        while (!availableRoles.isEmpty() && maxLoops > 0) {
            // If there's a role where only 1 out of remaining champions
            // can play it, match that
            for (int i = 0; i < availableRoles.size(); i++) {
                Role availableRole = availableRoles.get(i);
                Player potentialPlayer = null;
                for (Player availablePlayer : availablePlayers) {
                    Champion champion = availablePlayer.getChampionPlayed();
                    if (!champion.cannotPlayRole(availableRole)) {
                        if (potentialPlayer == null) {
                            potentialPlayer = availablePlayer;
                        } else {
                            potentialPlayer = null;
                            break;
                        }
                    }
                }
                if (potentialPlayer != null) {
                    map.put(potentialPlayer, availableRole);
                    availablePlayers.remove(potentialPlayer);
                    availableRoles.remove(i--);
                }
            }
            maxLoops--;
        }

        return availableRoles.size() != numAvailableRoles;
    }

    private boolean guessByChampionCanOnlyPreferOneRemainingRole(List<Player> availablePlayers, List<Role> availableRoles,
                                                                 Map<Player, Role> map) {
        int numAvailableRoles = availableRoles.size();

        int maxLoops = availableRoles.size();
        while (!availableRoles.isEmpty() && maxLoops > 0) {
            // If there's only 1 preferable role for a champion, take that one
            for (int i = 0; i < availablePlayers.size(); i++) {
                Player availablePlayer = availablePlayers.get(i);
                Champion champion = availablePlayer.getChampionPlayed();
                Role playableRole = null;
                for (Role availableRole : availableRoles) {
                    if (champion.shouldPlayRole(availableRole)) {
                        if (playableRole == null) {
                            playableRole = availableRole;
                        } else {
                            playableRole = null;
                            break;
                        }
                    }
                }
                if (playableRole != null) {
                    map.put(availablePlayer, playableRole);
                    availableRoles.remove(playableRole);
                    availablePlayers.remove(i--);
                }
            }
            maxLoops--;
        }

        return availableRoles.size() != numAvailableRoles;
    }

    private boolean guessByChampionCanOnlyPlayOneRemainingRole(List<Player> availablePlayers, List<Role> availableRoles,
                                                               Map<Player, Role> map) {
        int numAvailableRoles = availableRoles.size();

        int maxLoops = availableRoles.size();
        while (!availableRoles.isEmpty() && maxLoops > 0) {
            // If there's only 1 playable role for a champion, take that one
            for (int i = 0; i < availablePlayers.size(); i++) {
                Player availablePlayer = availablePlayers.get(i);
                Champion champion = availablePlayer.getChampionPlayed();
                Role playableRole = null;
                for (Role availableRole : availableRoles) {
                    if (!champion.cannotPlayRole(availableRole)) {
                        if (playableRole == null) {
                            playableRole = availableRole;
                        } else {
                            playableRole = null;
                            break;
                        }
                    }
                }
                if (playableRole != null) {
                    map.put(availablePlayer, playableRole);
                    availableRoles.remove(playableRole);
                    availablePlayers.remove(i--);
                }
            }
            maxLoops--;
        }

        return availableRoles.size() != numAvailableRoles;
    }
}
