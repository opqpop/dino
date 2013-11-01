package com.lolRiver.river.controllers.deserializers.videoDeserializers;

import com.lolRiver.river.models.Video;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/30/13
 */

public interface VideoDeserializer {
    public List<Video> videosFromJson(String json) throws Exception;
}
