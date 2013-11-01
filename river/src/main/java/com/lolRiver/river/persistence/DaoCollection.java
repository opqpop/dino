package com.lolRiver.river.persistence;

import com.lolRiver.river.persistence.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/2/13
 */

@Repository
public class DaoCollection {
    @Autowired
    private ClipDao clipDao;

    @Autowired
    private EloDao eloDao;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private LolUserDao lolUserDao;

    @Autowired
    private StreamerDao streamerDao;

    @Autowired
    protected VideoDao videoDao;

    public ClipDao getClipDao() {
        return clipDao;
    }

    public EloDao getEloDao() {
        return eloDao;
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public LolUserDao getLolUserDao() {
        return lolUserDao;
    }

    public StreamerDao getStreamerDao() {
        return streamerDao;
    }

    public VideoDao getVideoDao() {
        return videoDao;
    }
}
