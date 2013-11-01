package com.lolRiver.river.models;

import com.lolRiver.river.controllers.deserializers.appconstants.KassadinAppConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

public class SummonerSpell {
    private static final Logger LOGGER = Logger.getLogger(SummonerSpell.class.getName());

    public enum Name {
        BARRIER,
        CLAIRVOYANCE,
        CLARITY,
        CLEANSE,
        EXHAUST,
        FLASH,
        GARRISON,
        GHOST,
        HEAL,
        IGNITE,
        REVIVE,
        SMITE,
        TELEPORT
    }

    private Name name;

    public SummonerSpell(Name name) {
        this.name = name;
    }

    public static SummonerSpell fromId(String id) {
        SummonerSpell summonerSpell = new SummonerSpell(KassadinAppConstants.SUMMONER_SPELL_MAPPING.get(id));
        if (summonerSpell == null) {
            LOGGER.error("Got null summoner spell serializing from ID: " + id);
        }
        return summonerSpell;
    }

    public static SummonerSpell fromString(String spellName) {
        if (StringUtils.isBlank(spellName) || spellName.equals("null")) {
            // TODO xia turn this into a check when I know I can get all ss in future
            return null;
        }
        return new SummonerSpell(SummonerSpell.Name.valueOf(spellName));
    }

    public Name getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SummonerSpell{" +
               "name=" + name +
               '}';
    }
}
