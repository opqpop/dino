package com.lolRiver.river.persistence.interfaces;

import com.lolRiver.river.models.Clip;

import java.util.List;

/**
 * @author mxia (mxia@lolRiver.com)
 *         9/30/13
 */

public interface ClipDao {
    // returns number of clips in db
    public int getNumTotalClips();

    // returns number of clips in db
    public int getNumTotalClips(String orderBy, boolean descending, Clip clip);

    // get the Clip with given id
    public Clip getClipFromId(final int id);

    // get size clips from offset, ordered by orderBy
    public List<Clip> getClips(final int offset, final int size, String orderBy, boolean descending);

    // get clips based on conditions in clip
    public List<Clip> getClipsFromClip(final int offset, final int size, String orderBy, boolean descending,
                                       Clip clip);

    // create an Clip. Returns created Clip, or null if not possible
    public Clip insertClip(Clip clip);
}
