package com.lolRiver.river.persistence.interfaces;

import com.lolRiver.river.models.Streamer;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/1/13
 */

public interface StreamerDao {
    // get the Streamer with given id
    public Streamer getStreamerFromName(final String name);

    // get all streamers
    public List<Streamer> getStreamers();

    // create an Streamer. Returns created Streamer, or null if not possible
    public Streamer insertStreamer(Streamer streamer);
}
