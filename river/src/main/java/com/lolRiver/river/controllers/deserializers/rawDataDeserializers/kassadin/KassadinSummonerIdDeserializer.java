package com.lolRiver.river.controllers.deserializers.rawDataDeserializers.kassadin;

import com.jayway.jsonpath.JsonPath;
import com.lolRiver.river.controllers.deserializers.rawDataDeserializers.SummonerIdDeserializer;
import org.springframework.stereotype.Component;
/**
 * @author mxia (mxia@groupon.com)
 *         10/15/13
 */

@Component
public class KassadinSummonerIdDeserializer implements SummonerIdDeserializer {
    private final String SUMMONER_ID_PATH = "$.data.summonerId";

    @Override
    public int summonerIdFromJson(String json) throws Exception {
        return JsonPath.read(json, SUMMONER_ID_PATH);
    }
}
