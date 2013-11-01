package com.lolRiver.river.controllers.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/5/13
 */

@Repository
public class ConverterCollection {
    @Autowired
    private LeagueGameToGameConverter leagueGameToGameConverter;

    @Autowired
    private LeagueSummonerToLolUserConverter leagueSummonerToLolUserConverter;

    @Autowired
    private VideoToClipConverter videoToClipConverter;

    public LeagueGameToGameConverter getLeagueGameToGameConverter() {
        return leagueGameToGameConverter;
    }

    public LeagueSummonerToLolUserConverter getLeagueSummonerToLolUserConverter() {
        return leagueSummonerToLolUserConverter;
    }

    public VideoToClipConverter getVideoToClipConverter() {
        return videoToClipConverter;
    }
}
