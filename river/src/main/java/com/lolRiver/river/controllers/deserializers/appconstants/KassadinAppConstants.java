package com.lolRiver.river.controllers.deserializers.appconstants;

import com.lolRiver.river.models.Champion;
import com.lolRiver.river.models.SummonerSpell;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

public class KassadinAppConstants {
    public static final Map<String, Champion.Name> CHAMPION_MAPPING;
    public static final Map<String, SummonerSpell.Name> SUMMONER_SPELL_MAPPING;

    static {
        CHAMPION_MAPPING = Collections.unmodifiableMap(championMapping());
        SUMMONER_SPELL_MAPPING = Collections.unmodifiableMap(summonerSpellMapping());
    }

    // TODO remove this since it's captured in LeagueChampion
    private static Map<String, Champion.Name> championMapping() {
        Map<String, Champion.Name> map = new HashMap<String, Champion.Name>();
        map.put("266", Champion.Name.AATROX);
        map.put("103", Champion.Name.AHRI);
        map.put("84", Champion.Name.AKALI);
        map.put("12", Champion.Name.ALISTAR);
        map.put("32", Champion.Name.AMUMU);
        map.put("34", Champion.Name.ANIVIA);
        map.put("1", Champion.Name.ANNIE);
        map.put("22", Champion.Name.ASHE);
        map.put("53", Champion.Name.BLITZCRANK);
        map.put("63", Champion.Name.BRAND);
        map.put("51", Champion.Name.CAITLYN);
        map.put("69", Champion.Name.CASSIOPEIA);
        map.put("31", Champion.Name.CHOGATH);
        map.put("42", Champion.Name.CORKI);
        map.put("122", Champion.Name.DARIUS);
        map.put("131", Champion.Name.DIANA);
        map.put("119", Champion.Name.DRAVEN);
        map.put("36", Champion.Name.DRMUNDO);
        map.put("60", Champion.Name.ELISE);
        map.put("28", Champion.Name.EVELYNN);
        map.put("81", Champion.Name.EZREAL);
        map.put("9", Champion.Name.FIDDLESTICKS);
        map.put("114", Champion.Name.FIORA);
        map.put("105", Champion.Name.FIZZ);
        map.put("3", Champion.Name.GALIO);
        map.put("41", Champion.Name.GANGPLANK);
        map.put("86", Champion.Name.GAREN);
        map.put("79", Champion.Name.GRAGAS);
        map.put("104", Champion.Name.GRAVES);
        map.put("120", Champion.Name.HECARIM);
        map.put("74", Champion.Name.HEIMERDINGER);
        map.put("39", Champion.Name.IRELIA);
        map.put("40", Champion.Name.JANNA);
        map.put("59", Champion.Name.JARVANIV);
        map.put("24", Champion.Name.JAX);
        map.put("126", Champion.Name.JAYCE);
        map.put("222", Champion.Name.JINX);
        map.put("43", Champion.Name.KARMA);
        map.put("30", Champion.Name.KARTHUS);
        map.put("38", Champion.Name.KASSADIN);
        map.put("55", Champion.Name.KATARINA);
        map.put("10", Champion.Name.KAYLE);
        map.put("85", Champion.Name.KENNEN);
        map.put("121", Champion.Name.KHAZIX);
        map.put("96", Champion.Name.KOGMAW);
        map.put("7", Champion.Name.LEBLANC);
        map.put("64", Champion.Name.LEESIN);
        map.put("89", Champion.Name.LEONA);
        map.put("127", Champion.Name.LISSANDRA);
        map.put("236", Champion.Name.LUCIAN);
        map.put("117", Champion.Name.LULU);
        map.put("99", Champion.Name.LUX);
        map.put("54", Champion.Name.MALPHITE);
        map.put("90", Champion.Name.MALZAHAR);
        map.put("57", Champion.Name.MAOKAI);
        map.put("11", Champion.Name.MASTERYI);
        map.put("21", Champion.Name.MISSFORTUNE);
        map.put("82", Champion.Name.MORDEKAISER);
        map.put("25", Champion.Name.MORGANA);
        map.put("267", Champion.Name.NAMI);
        map.put("75", Champion.Name.NASUS);
        map.put("111", Champion.Name.NAUTILUS);
        map.put("76", Champion.Name.NIDALEE);
        map.put("56", Champion.Name.NOCTURNE);
        map.put("20", Champion.Name.NUNU);
        map.put("2", Champion.Name.OLAF);
        map.put("61", Champion.Name.ORIANNA);
        map.put("80", Champion.Name.PANTHEON);
        map.put("78", Champion.Name.POPPY);
        map.put("133", Champion.Name.QUINN);
        map.put("33", Champion.Name.RAMMUS);
        map.put("58", Champion.Name.RENEKTON);
        map.put("107", Champion.Name.RENGAR);
        map.put("92", Champion.Name.RIVEN);
        map.put("68", Champion.Name.RUMBLE);
        map.put("13", Champion.Name.RYZE);
        map.put("113", Champion.Name.SEJUANI);
        map.put("35", Champion.Name.SHACO);
        map.put("98", Champion.Name.SHEN);
        map.put("102", Champion.Name.SHYVANA);
        map.put("27", Champion.Name.SINGED);
        map.put("14", Champion.Name.SION);
        map.put("15", Champion.Name.SIVIR);
        map.put("72", Champion.Name.SKARNER);
        map.put("37", Champion.Name.SONA);
        map.put("16", Champion.Name.SORAKA);
        map.put("50", Champion.Name.SWAIN);
        map.put("134", Champion.Name.SYNDRA);
        map.put("91", Champion.Name.TALON);
        map.put("44", Champion.Name.TARIC);
        map.put("17", Champion.Name.TEEMO);
        map.put("412", Champion.Name.THRESH);
        map.put("18", Champion.Name.TRISTANA);
        map.put("48", Champion.Name.TRUNDLE);
        map.put("23", Champion.Name.TRYNDAMERE);
        map.put("4", Champion.Name.TWISTEDFATE);
        map.put("29", Champion.Name.TWITCH);
        map.put("77", Champion.Name.UDYR);
        map.put("6", Champion.Name.URGOT);
        map.put("110", Champion.Name.VARUS);
        map.put("67", Champion.Name.VAYNE);
        map.put("45", Champion.Name.VEIGAR);
        map.put("254", Champion.Name.VI);
        map.put("112", Champion.Name.VIKTOR);
        map.put("8", Champion.Name.VLADMIR);
        map.put("106", Champion.Name.VOLIBEAR);
        map.put("19", Champion.Name.WARWICK);
        map.put("62", Champion.Name.WUKONG);
        map.put("101", Champion.Name.XERATH);
        map.put("5", Champion.Name.XINZHAO);
        map.put("83", Champion.Name.YORICK);
        map.put("154", Champion.Name.ZAC);
        map.put("238", Champion.Name.ZED);
        map.put("115", Champion.Name.ZIGGS);
        map.put("26", Champion.Name.ZILEAN);
        map.put("143", Champion.Name.ZYRA);
        return map;
    }

    private static Map<String, SummonerSpell.Name> summonerSpellMapping() {
        Map<String, SummonerSpell.Name> map = new HashMap<String, SummonerSpell.Name>();
        map.put("21", SummonerSpell.Name.BARRIER);
        map.put("1", SummonerSpell.Name.CLEANSE);
        map.put("2", SummonerSpell.Name.CLAIRVOYANCE);
        map.put("14", SummonerSpell.Name.IGNITE);
        map.put("3", SummonerSpell.Name.EXHAUST);
        map.put("4", SummonerSpell.Name.FLASH);
        map.put("6", SummonerSpell.Name.GHOST);
        map.put("7", SummonerSpell.Name.HEAL);
        map.put("17", SummonerSpell.Name.GARRISON);
        map.put("10", SummonerSpell.Name.REVIVE);
        map.put("11", SummonerSpell.Name.SMITE);
        map.put("12", SummonerSpell.Name.TELEPORT);
        return map;
    }
}
