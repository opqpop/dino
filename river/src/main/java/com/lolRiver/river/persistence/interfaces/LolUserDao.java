package com.lolRiver.river.persistence.interfaces;

import com.lolRiver.river.models.LolUser;
import com.lolRiver.river.models.Streamer;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/1/13
 */

public interface LolUserDao {
    // get the LolUser with given id
    public LolUser getLolUserFromId(final int id);

    // get all lol users
    public List<LolUser> getLolUsers();

    // get the LolUsers belong to streamer
    public List<LolUser> getLolUsersFromStreamer(final Streamer streamer);

    // create an LolUser. Returns created LolUser, or null if not possible
    public LolUser insertLolUser(LolUser lolUser);
}
