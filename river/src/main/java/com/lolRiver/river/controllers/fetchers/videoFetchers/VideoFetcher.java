package com.lolRiver.river.controllers.fetchers.videoFetchers;

import com.lolRiver.river.models.Streamer;

import java.util.List;

/**
 * @author mxia (mxia@lolRiver.com)
 *         8/6/13
 */

public interface VideoFetcher {
    public void fetchAndStoreVideos(List<Streamer> streamers);
}
