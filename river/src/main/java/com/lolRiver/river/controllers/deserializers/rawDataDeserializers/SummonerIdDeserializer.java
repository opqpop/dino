package com.lolRiver.river.controllers.deserializers.rawDataDeserializers;

/**
 * @author mxia (mxia@groupon.com)
 *         10/15/13
 */

public interface SummonerIdDeserializer {
    public int summonerIdFromJson(String json) throws Exception;
}
