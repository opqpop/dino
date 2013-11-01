package com.lolRiver.river.controllers.deserializers.rawDataDeserializers;

import com.lolRiver.river.models.Game;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/28/13
 */

public interface GameDeserializer {
    public List<Game> gamesFromJson(String json) throws Exception;
}
