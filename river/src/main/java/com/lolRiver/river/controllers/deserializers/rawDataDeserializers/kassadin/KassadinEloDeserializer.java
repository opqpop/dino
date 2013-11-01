package com.lolRiver.river.controllers.deserializers.rawDataDeserializers.kassadin;

import com.jayway.jsonpath.JsonPath;
import com.lolRiver.river.controllers.deserializers.rawDataDeserializers.EloDeserializer;
import com.lolRiver.river.models.Elo;
import com.lolRiver.river.util.DateUtil;
import com.lolRiver.river.util.NumbersUtil;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/3/13
 */

@Component
public class KassadinEloDeserializer implements EloDeserializer {
    private final String ELOS_PATH = "$.data.summonerLeagues.array";
    private final String ELO_TYPE_PATH = "queue";
    private final String TIER_PATH = "tier";
    private final String DIVISION_PATH = "requestorsRank";

    private String eloPath(int eloIndex) {
        return ELOS_PATH + "[" + eloIndex + "]";
    }

    private String eloType(int eloIndex, String json) {
        return JsonPath.read(json, eloPath(eloIndex) + "." + ELO_TYPE_PATH);
    }

    private String tier(int eloIndex, String json) {
        return JsonPath.read(json, eloPath(eloIndex) + "." + TIER_PATH);
    }

    private String division(int eloIndex, String json) {
        return JsonPath.read(json, eloPath(eloIndex) + "." + DIVISION_PATH);
    }

    private String eloNameString(String tier, String division) {
        if (tier.equals("CHALLENGER")) {
            return tier;
        }
        return tier + NumbersUtil.romanNumeralToNumber(division);
    }

    @Override
    public Elo eloFromJson(String json) {
        List elosFromJson = JsonPath.read(json, ELOS_PATH);
        for (int i = 0; i < elosFromJson.size(); i++) {
            String eloType = eloType(i, json);
            if (eloType.equals("RANKED_SOLO_5x5")) {
                String tier = tier(i, json);
                String division = division(i, json);
                return Elo.fromString(eloNameString(tier, division))
                        .setTime(DateUtil.getCurrentTimestamp());
            }
        }
        return null;
    }
}
