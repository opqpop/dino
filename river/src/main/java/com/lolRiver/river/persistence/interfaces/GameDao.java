package com.lolRiver.river.persistence.interfaces;

import com.lolRiver.river.models.Game;
import com.lolRiver.river.models.Video;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

public interface GameDao {
    // get the Game with given id
    public Game getGameFromId(final int id);

    // get the latest game
    public Game getLatestGame();

    // get all games that occured while streamer was streaming video
    public List<Game> getGamesMatchingVideo(Video video);

    // create an Game. Returns created Game, or null if not possible
    public Game insertGame(Game game);

    // update an Game. Returns true if successful
    public boolean updateGame(Game game);
}
