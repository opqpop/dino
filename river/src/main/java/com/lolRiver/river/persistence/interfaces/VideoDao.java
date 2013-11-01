package com.lolRiver.river.persistence.interfaces;

import com.lolRiver.river.models.State;
import com.lolRiver.river.models.Video;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/30/13
 */

public interface VideoDao {
    // get the Video with given id
    public Video getVideoFromId(final int id);

    public List<Video> getUncovertedVideos();

    // create an Video. Returns created Video, or null if not possible
    public Video insertVideo(Video video);

    // update an Video with given id to given state. Returns true if successful.
    public boolean updateVideoState(int id, State state);
}
