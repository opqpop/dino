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


package com.lolRiver.achimala.leaguelib.services;

import com.lolRiver.achimala.leaguelib.connection.LeagueConnection;
import com.lolRiver.achimala.leaguelib.models.LeagueCompetitiveSeason;
import com.lolRiver.achimala.leaguelib.models.LeagueSummoner;
import com.lolRiver.achimala.leaguelib.models.MatchHistoryEntry;
import com.lolRiver.achimala.util.Callback;
import com.lolRiver.rtmp.TypedObject;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsService extends LeagueAbstractService {
    private final String SUMMONERS_RIFT = "CLASSIC";

    public PlayerStatsService(LeagueConnection connection) {
        super(connection);
    }

    public String getServiceName() {
        return "playerStatsService";
    }

    public void fillRankedStats(final LeagueSummoner summoner, final Callback<LeagueSummoner> callback) {
        callAsynchronously("getAggregatedStats", new Object[]{summoner.getAccountId(), SUMMONERS_RIFT, LeagueCompetitiveSeason.CURRENT.toString()}, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                try {
                    //summoner.setRankedStats(new LeagueSummonerRankedStats(obj.getTO("body")));
                    callback.onCompletion(summoner);
                } catch (Exception ex) {
                    callback.onError(ex);
                }
            }

            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    private List<MatchHistoryEntry> getMatchHistoryEntriesFromResult(TypedObject obj, LeagueSummoner summoner) {
        Object[] games = obj.getTO("body").getArray("gameStatistics");
        if (games == null || games.length == 0)
            // No match history available; return an empty list
            return new ArrayList<MatchHistoryEntry>();
        List<MatchHistoryEntry> recentGames = new ArrayList<MatchHistoryEntry>();
        for (Object game : games)
            recentGames.add(new MatchHistoryEntry((TypedObject)game, summoner));
        return recentGames;
    }

    public void fillMatchHistory(final LeagueSummoner summoner, final Callback<LeagueSummoner> callback) {
        callAsynchronously("getRecentGames", new Object[]{summoner.getAccountId()}, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                summoner.setMatchHistory(getMatchHistoryEntriesFromResult(obj, summoner));
                callback.onCompletion(summoner);
            }

            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }
}