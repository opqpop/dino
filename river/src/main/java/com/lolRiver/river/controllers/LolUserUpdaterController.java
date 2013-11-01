package com.lolRiver.river.controllers;

import com.lolRiver.achimala.leaguelib.MainClient;
import com.lolRiver.achimala.leaguelib.connection.LeagueServer;
import com.lolRiver.river.controllers.fetchers.rawDataFetchers.KassadinRawDataFetcher;
import com.lolRiver.river.models.LolUser;
import com.lolRiver.river.models.Streamer;
import com.lolRiver.river.persistence.DaoCollection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mxia (mxia@lolRiver.com)
 *         10/3/13
 */

@Controller
// TODO make this possible to be called by me only since it issues 100+ calls to kassadin each time
public class LolUserUpdaterController {
    private static final Logger LOGGER = Logger.getLogger(LolUserUpdaterController.class.getName());

    @Autowired
    DaoCollection daoCollection;

    @Autowired
    KassadinRawDataFetcher rawDataFetcher;

    @RequestMapping(value = {"/updateUsers"}, method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public ResponseEntity<String> start() throws Exception {
        Map<LeagueServer, List<LolUser>> usersToAdd = new HashMap<LeagueServer, List<LolUser>>();

        // na
        List<LolUser> naUsers = new ArrayList<LolUser>();
        // tests
        naUsers.add(new LolUser("markdonkey", "mark donkey", "na"));

        // TSM
        naUsers.add(new LolUser("tsm_dyrus", "Dyrus", "na"));
        naUsers.add(new LolUser("tsm_wildturtle", "TSM CAT", "na"));
        naUsers.add(new LolUser("tsm_wildturtle", "WildTurtle", "na"));
        naUsers.add(new LolUser("tsm_xpecial", "Xpecial", "na"));
        naUsers.add(new LolUser("tsm_theoddone", "TheOddOne", "na"));
        naUsers.add(new LolUser("tsm_reginald", "Reginald", "na"));
        naUsers.add(new LolUser("dandinh", "Man Dinh", "na"));
        naUsers.add(new LolUser("ninjakenlol", "ninjaken", "na"));
        // C9
        naUsers.add(new LolUser("manballs", "C9 Squirtle", "na"));
        naUsers.add(new LolUser("hail9", "C9 HyperX Diego", "na"));
        naUsers.add(new LolUser("hail9", "C9 Hai", "na"));
        naUsers.add(new LolUser("meteos", "C9 Meteos", "na"));
        naUsers.add(new LolUser("meteos", "C10 Meteos", "na"));
        // Vulcun
        naUsers.add(new LolUser("mancloud", "V ilymancloud", "na"));
        naUsers.add(new LolUser("xmithie", "V AleXmithie", "na"));
        naUsers.add(new LolUser("xmithie", "Xminie", "na"));
        // Dignitas
        naUsers.add(new LolUser("crumbzz", "d Crumbz", "na"));
        naUsers.add(new LolUser("crumbzz", "Prof Crumbledore", "na"));
        naUsers.add(new LolUser("imaqtpie", "imaqtpie", "na"));
        naUsers.add(new LolUser("scarra", "scarra", "na"));
        // Crs
        naUsers.add(new LolUser("voyboy", "Crs Vooby", "na"));
        naUsers.add(new LolUser("crs_saintvicious", "Crs SantaVicious", "na"));
        naUsers.add(new LolUser("edwardlol", "Edward Carry", "na"));
        naUsers.add(new LolUser("cop", "crs goompa", "na"));
        naUsers.add(new LolUser("cop", "Tritan", "na"));
        naUsers.add(new LolUser("nyjacky", "Crs Bear", "na"));
        naUsers.add(new LolUser("pobelter", "intero", "na"));
        naUsers.add(new LolUser("pobelter", "impobelter", "na"));
        naUsers.add(new LolUser("zekent", "Zekent Time", "na"));
        naUsers.add(new LolUser("zekent", "Zékent", "na"));
        // CST
        naUsers.add(new LolUser("dontmashme", "dontmashme", "na"));
        naUsers.add(new LolUser("zionspartan", "ZionSpartan", "na"));
        naUsers.add(new LolUser("tdnintendudex", "NintendudeX", "na"));
        naUsers.add(new LolUser("tdnintendudex", "Nintenpai", "na"));
        naUsers.add(new LolUser("daydreaminyo", "Daydreamin", "na"));
        naUsers.add(new LolUser("shiphtur", "CST Shiponya", "na"));
        naUsers.add(new LolUser("jintaee", "atkwave", "na"));
        naUsers.add(new LolUser("jintaee", "Jintae", "na"));
        // GGLA
        naUsers.add(new LolUser("bischulol", "Bıschu", "na"));
        naUsers.add(new LolUser("quaslol", "gg quas", "na"));
        naUsers.add(new LolUser("quaslol", "dr quas", "na"));

        // misc
        naUsers.add(new LolUser("bestrivenna", "Best Riven NA", "na"));
        naUsers.add(new LolUser("phantoml0rd", "PhantomL0rd", "na"));
        naUsers.add(new LolUser("robertxlee", "ROBERTxGEEGEE", "na"));
        naUsers.add(new LolUser("chaoxlol", "Chaox", "na"));
        naUsers.add(new LolUser("therainman", "The Rain Man", "na"));
        naUsers.add(new LolUser("chu8", "chu8", "na"));
        naUsers.add(new LolUser("chu8", "AlexHori", "na"));
        naUsers.add(new LolUser("hashinshin", "hashinshin", "na"));
        naUsers.add(new LolUser("rflegendary", "RF Legendary", "na"));
        naUsers.add(new LolUser("ikarry", "iKarry", "na"));
        naUsers.add(new LolUser("hotshotgg", "HotShotGG", "na"));
        naUsers.add(new LolUser("aphromoo", "aphromoo", "na"));
        naUsers.add(new LolUser("aphromoo", "Zeemeeandye", "na"));
        naUsers.add(new LolUser("lolgeranimo", "Geranimo", "na"));
        naUsers.add(new LolUser("lolgeranimo", "KittenCannon420", "na"));
        naUsers.add(new LolUser("trick2g", "best volidyr na", "na"));
        naUsers.add(new LolUser("trick2g", "KING TRICK", "na"));
        naUsers.add(new LolUser("cadburry", "Good Old Tom", "na"));
        naUsers.add(new LolUser("cadburry", "OH IVE SEEN", "na"));
        naUsers.add(new LolUser("grossie_gore", "Gross Gore", "na"));
        naUsers.add(new LolUser("cruzerthebruzer", "Cruzerthebruzer", "na"));
        naUsers.add(new LolUser("sirhcez", "SirhcEzZz", "na"));
        naUsers.add(new LolUser("sirhcez", "Hehm", "na"));
        naUsers.add(new LolUser("sirhcez", "sirhcez", "na"));
        naUsers.add(new LolUser("dioudlol", "CJ Entus MadLife", "na"));
        naUsers.add(new LolUser("phreakstream", "RiotPhreak", "na"));
        naUsers.add(new LolUser("sovitia", "sovitia", "na"));
        naUsers.add(new LolUser("paperbat", "pbat", "na"));
        naUsers.add(new LolUser("iijeriichoii", "ii jeriicho ii", "na"));
        naUsers.add(new LolUser("mushisgosu", "hi im mush", "na"));
        naUsers.add(new LolUser("mushisgosu", "hi im gosu", "na"));
        naUsers.add(new LolUser("nightblue3", "nightblue3", "na"));
        naUsers.add(new LolUser("itshafu", "Wildfire Zyra", "na"));
        naUsers.add(new LolUser("sky_mp3", "Śký", "na"));
        naUsers.add(new LolUser("worstsingedus", "worst smurff us", "na"));
        naUsers.add(new LolUser("worstsingedus", "worst singed us", "na"));

        // euw
        List<LolUser> euwUsers = new ArrayList<LolUser>();
        // fanatic
        euwUsers.add(new LolUser("fnaticcyanide", "Fnatic Cyanide", "euw"));
        euwUsers.add(new LolUser("puszu", "Fnatic Puszu", "euw"));
        euwUsers.add(new LolUser("soazed", "Fnatic s0AZ", "euw"));
        euwUsers.add(new LolUser("xpeke", "Fnatic xMid", "euw"));
        // gambit
        euwUsers.add(new LolUser("alexich", "GG BenQ Alex Ich", "euw"));
        euwUsers.add(new LolUser("tehdiamondz", "GG BenQ Diamond", "euw"));
        // eg
        euwUsers.add(new LolUser("froggen", "Froggen", "euw"));
        euwUsers.add(new LolUser("froggen", "Egg of Froggen", "euw"));
        euwUsers.add(new LolUser("froggen", "You got Frogged", "euw"));
        euwUsers.add(new LolUser("wickd", "APWickd", "euw"));
        euwUsers.add(new LolUser("skumbagkrepo", "Krepton1te", "euw"));
        euwUsers.add(new LolUser("skumbagkrepo", "Krepo", "euw"));
        // lemondogs
        euwUsers.add(new LolUser("tabzzhd", "Suigíntou", "euw"));
        euwUsers.add(new LolUser("tabzzhd", "Ld Tabzz", "euw"));
        // misc
        euwUsers.add(new LolUser("guardsmanbob", "Guardsman Bob", "euw"));
        euwUsers.add(new LolUser("wrecklol", "ES Wreck", "euw"));
        euwUsers.add(new LolUser("edwardlol", "Edward Carry", "euw"));
        euwUsers.add(new LolUser("darkpassagemedia", "DP Naru", "euw"));
        naUsers.add(new LolUser("holythoth", "DP HolyPhoenix", "euw"));
        naUsers.add(new LolUser("holythoth", "DP HolyThoth", "euw"));

        // br
        List<LolUser> brUsers = new ArrayList<LolUser>();
        brUsers.add(new LolUser("painbrtt", "Eminem TT", "br"));
        brUsers.add(new LolUser("painbrtt", "paiN brTT Razer", "br"));
        naUsers.add(new LolUser("xuuuura", "xuuura", "br"));

        // add all users
        usersToAdd.put(LeagueServer.NORTH_AMERICA, naUsers);
        usersToAdd.put(LeagueServer.EUROPE_WEST, euwUsers);
        usersToAdd.put(LeagueServer.BRAZIL, brUsers);

        int totalUsers = 0;
        for (List<LolUser> lolUsers : usersToAdd.values()) {
            totalUsers += lolUsers.size();
        }
        LOGGER.info("Total users: " + totalUsers + "\n");

        // add all users to streamers table
        for (List<LolUser> lolUsers : usersToAdd.values()) {
            for (LolUser lolUser : lolUsers) {
                Streamer streamer = new Streamer(lolUser.getStreamerName());
                daoCollection.getStreamerDao().insertStreamer(streamer);

                int summonerId = rawDataFetcher.fetchSummonerId(lolUser.getUsername(), lolUser.getRegion());
                lolUser.setId(summonerId);
                daoCollection.getLolUserDao().insertLolUser(lolUser);
            }
        }

        // TODO delete this once I'm confident I don't need main client to look up user summoner ids
        // We use 3rd party API instead of this because it can't get ids of some users like xuuura @ br
        //        for (LeagueServer leagueServer : usersToAdd.keySet()) {
        //            try {
        //                MainClient mainClient = new MainClient();
        //                mainClient.retrieveUserInfo(leagueServer, usersToAdd.get(leagueServer), daoCollection);
        //            } catch (Exception e) {
        //                LOGGER.error("error retrieving lolUsers on server: " + leagueServer);
        //            }
        //        }

        return new ResponseEntity<String>("Updated users. Total users: " + totalUsers, HttpStatus.OK);
    }
}
