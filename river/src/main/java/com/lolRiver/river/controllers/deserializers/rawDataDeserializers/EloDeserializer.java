package com.lolRiver.river.controllers.deserializers.rawDataDeserializers;

import com.lolRiver.river.models.Elo;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/3/13
 */

public interface EloDeserializer {
    public Elo eloFromJson(String json) throws Exception;
}
