package com.lolRiver.river.persistence.interfaces;

import com.lolRiver.river.models.Elo;

import java.util.Map;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/1/13
 */

public interface EloDao {
    // get the Elo with given id
    public Elo getEloFromId(final int id);

    // create an Elo. Returns created Elo, or null if not possible
    public Elo insertElo(Elo elo);

    // get the Elos matching elo's paramsToMatch
    public Elo getLatestEloFromElo(Elo elo, Map<String, String> paramsToMatch);
}
