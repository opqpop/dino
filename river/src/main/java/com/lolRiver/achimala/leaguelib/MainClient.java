package com.lolRiver.achimala.leaguelib;

import com.lolRiver.achimala.leaguelib.connection.AccountConnectorRunnable;
import com.lolRiver.achimala.leaguelib.connection.LeagueAccount;
import com.lolRiver.achimala.leaguelib.connection.LeagueConnection;
import com.lolRiver.achimala.leaguelib.connection.LeagueServer;
import com.lolRiver.achimala.leaguelib.errors.LeagueException;
import com.lolRiver.achimala.leaguelib.models.LeagueGame;
import com.lolRiver.achimala.leaguelib.models.LeagueSummoner;
import com.lolRiver.achimala.leaguelib.models.LeagueSummonerLeagueStats;
import com.lolRiver.achimala.util.Callback;
import com.lolRiver.config.ConfigMap;
import com.lolRiver.river.controllers.converters.ConverterCollection;
import com.lolRiver.river.models.Game;
import com.lolRiver.river.models.LolUser;
import com.lolRiver.river.persistence.DaoCollection;
import com.lolRiver.river.util.DateUtil;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mxia (mxia@lolRiver.com)
 *         10/6/13
 */

public class MainClient {
    private static final Logger LOGGER = Logger.getLogger(MainClient.class.getName());
    private static final String CLIENT_VERSION = new ConfigMap().getString("riot.client_version");
    private static final String BOTS_FILE = new ConfigMap().getString("riot.bots_file");
    private static final boolean DEBUG_MODE = true;
    private static int CONNECTION_WAIT_TIME = 20000;  // wait 20 seconds for all connections to connect

    private int count = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition done = lock.newCondition();
    private int numUsersInGame = 0;

    private void incrementCount() {
        lock.lock();
        count++;
        if (DEBUG_MODE) {
            System.out.println("+ count = " + count);
        }
        lock.unlock();
    }

    private void decrementCount() {
        lock.lock();
        count--;
        if (count == 0) {
            done.signal();
        }
        if (DEBUG_MODE) {
            System.out.println("- count = " + count);
        }
        lock.unlock();
    }

    public LeagueConnection getConnectionForServer(LeagueServer region) throws Exception {
        final LeagueConnection connection = new LeagueConnection(region);

        InputStream botInput = Thread.currentThread().getContextClassLoader().getResourceAsStream(BOTS_FILE);
        Map<String, Object> botObject = (Map<String, Object>)new Yaml().load(botInput);
        List<Map<String, String>> bots = (List<Map<String, String>>)botObject.get(region.getServerCode().toLowerCase());

        for (Map<String, String> bot : bots) {
            String username = bot.get("username");
            String password = bot.get("password");
            LOGGER.debug("Attempting to connect account: (" + username + " // " + password + ")");

            LeagueAccount leagueAccount = new LeagueAccount(region, CLIENT_VERSION, username, password);
            Thread thread = new Thread(new AccountConnectorRunnable(connection, leagueAccount));
            thread.start();
        }
        Thread.sleep(CONNECTION_WAIT_TIME);
        return connection;
    }

    // clientVersion is number at top left corner of your client
    public void retrieve(LeagueServer region, List<LolUser> lolUsers, final DaoCollection daoCollection,
                         final ConverterCollection converterCollection) throws Exception {
        LOGGER.info("Retrieving data at region: " + region + " , Obtaining Connection...");
        final LeagueConnection c = getConnectionForServer(region);

        try {
            LOGGER.debug("Looking up users...");
            lock.lock();
            numUsersInGame = 0;
            for (final LolUser lolUser : lolUsers) {
                LOGGER.debug("Looking up user..." + lolUser);
                try {
                    final String SUMMONER_TO_LOOK_UP = lolUser.getUsername();

                    incrementCount();
                    c.getSummonerService().getSummonerByName(SUMMONER_TO_LOOK_UP, new Callback<LeagueSummoner>() {
                        public void onCompletion(LeagueSummoner summoner) {

                            lock.lock();

                            LOGGER.debug(summoner.getName() + ":");
                            LOGGER.debug("    accountID:  " + summoner.getAccountId());
                            LOGGER.debug("    summonerID: " + summoner.getId());

                            incrementCount();
                            LOGGER.debug("Getting profile data..." + lolUser);
                            c.getSummonerService().fillPublicSummonerData(summoner, new Callback<LeagueSummoner>() {
                                public void onCompletion(LeagueSummoner summoner) {
                                    lock.lock();
                                    LOGGER.debug("Successfully got profile data: " + lolUser);
                                    LOGGER.debug("    S1: " + summoner.getProfileInfo().getSeasonOneTier());
                                    LOGGER.debug("    S2: " + summoner.getProfileInfo().getSeasonTwoTier());
                                    decrementCount();
                                    lock.unlock();
                                }

                                public void onError(Exception ex) {
                                    lock.lock();
                                    LOGGER.error(ex.getMessage());
                                    decrementCount();
                                    lock.unlock();
                                }
                            });

                            incrementCount();
                            LOGGER.debug("Getting leagues data..." + lolUser);
                            c.getLeaguesService().fillSoloQueueLeagueData(summoner, new Callback<LeagueSummoner>() {
                                public void onCompletion(LeagueSummoner summoner) {
                                    lock.lock();
                                    LOGGER.debug("Successfully got league data: " + lolUser);
                                    LeagueSummonerLeagueStats stats = summoner.getLeagueStats();
                                    LOGGER.debug(stats == null ? "NOT IN LEAGUE" : stats);
                                    decrementCount();
                                    lock.unlock();
                                }

                                public void onError(Exception ex) {
                                    lock.lock();
                                    LOGGER.error(ex.getMessage());
                                    decrementCount();
                                    lock.unlock();
                                }
                            });

                            // TODO xia this causes the client to hang for some reason
                            // not important to my website so I just commented it out (couldn't figure out the reason)
                        /*incrementCount();
                        LOGGER.debug("Getting champ data..." + lolUser);
                        c.getPlayerStatsService().fillRankedStats(summoner, new Callback<LeagueSummoner>() {
                            public void onCompletion(LeagueSummoner summoner) {
                                lock.lock();
                                LOGGER.debug("Successfully got champ data: " + lolUser);
                                decrementCount();
                                lock.unlock();
                            }

                            public void onError(Exception ex) {
                                lock.lock();
                                LOGGER.error(ex.getMessage());
                                decrementCount();
                                lock.unlock();
                            }
                        }); */

                            incrementCount();
                            LOGGER.debug("Getting game data..." + lolUser);
                            c.getGameService().fillActiveGameData(summoner, new Callback<LeagueSummoner>() {
                                public void onCompletion(LeagueSummoner summoner) {
                                    lock.lock();
                                    LOGGER.debug("Successfully got active game..." + lolUser);
                                    if (summoner.getActiveGame() != null) {
                                        LOGGER.info("IN GAME: " + lolUser);
                                        numUsersInGame++;
                                        LeagueGame game = summoner.getActiveGame();
                                        insertGameToDb(game, daoCollection, converterCollection);
                                    } else {
                                        LOGGER.info("NOT IN GAME: " + lolUser);
                                    }
                                    decrementCount();
                                    lock.unlock();
                                }

                                public void onError(Exception ex) {
                                    lock.lock();
                                    if (ex instanceof LeagueException) {
                                        LOGGER.error(((LeagueException)ex).getErrorCode());
                                    } else {
                                        LOGGER.error(ex.getMessage());
                                    }
                                    decrementCount();
                                    lock.unlock();
                                }
                            });

                            incrementCount();
                            LOGGER.debug("Getting game data..." + lolUser);
                            c.getPlayerStatsService().fillMatchHistory(summoner, new Callback<LeagueSummoner>() {
                                public void onCompletion(LeagueSummoner summoner) {
                                    lock.lock();
                                    LOGGER.debug("Successfully filled recent games..." + lolUser);
                                    //                                if (summoner.getActiveGame() != null) {
                                    //                                    LOGGER.info("IN GAME: " + lolUser);
                                    //                                    numUsersInGame++;
                                    //                                    LeagueGame game = summoner.getActiveGame();
                                    //                                    insertGameToDb(game, daoCollection, converterCollection);
                                    //                                } else {
                                    //                                    LOGGER.info("NOT IN GAME: " + lolUser);
                                    //                                }
                                    decrementCount();
                                    lock.unlock();
                                }

                                public void onError(Exception ex) {
                                    lock.lock();
                                    if (ex instanceof LeagueException) {
                                        LOGGER.error(((LeagueException)ex).getErrorCode());
                                    } else {
                                        LOGGER.error(ex.getMessage());
                                    }
                                    decrementCount();
                                    lock.unlock();
                                }
                            });

                            decrementCount();
                            lock.unlock();
                        }

                        public void onError(Exception ex) {
                            lock.lock();
                            LOGGER.error(ex);
                            decrementCount();
                            lock.unlock();
                        }
                    });
                } catch (Exception e) {
                    LOGGER.error("Error trying to retrieve data from riot for user: " + lolUser, e);
                }
            }
            Timestamp currentTime = DateUtil.getCurrentTimestamp();
            System.out.println(currentTime + ": Waiting for API calls to finish for region: " + region);
            LOGGER.debug("Waiting for API calls to finish for region: " + region);
            done.await();
            currentTime = DateUtil.getCurrentTimestamp();
            System.out.println(currentTime + ": Found " + numUsersInGame + " users in games");
            System.out.println(currentTime + ": API calls successfully finished for region: " + region);
            LOGGER.info("Found " + numUsersInGame + " users in games");
            LOGGER.info("API calls successfully finished for region: " + region);
            lock.unlock();
        } finally {
            closeAccounts(c);
        }
    }

    public void insertGameToDb(LeagueGame leagueGame, DaoCollection daoCollection, ConverterCollection converterCollection) {
        Game game = converterCollection.getLeagueGameToGameConverter().convert(leagueGame);
        if (game == null) {
            LOGGER.error("Tried to insert null game from leagueGame: " + leagueGame);
            return;
        }
        daoCollection.getGameDao().insertGame(game);
    }

    public void retrieveUserInfo(LeagueServer region, final List<LolUser> lolUsers,
                                 final DaoCollection daoCollection) throws Exception {
        LeagueConnection c = getConnectionForServer(region);

        lock.lock();
        for (final LolUser lolUser : lolUsers) {
            final String SUMMONER_TO_LOOK_UP = lolUser.getUsername();
            Map<LeagueAccount, LeagueException> exceptions = c.getAccountQueue().connectAll();
            if (exceptions != null) {
                for (LeagueAccount account : exceptions.keySet())
                    LOGGER.error(account + " error: " + exceptions.get(account));
                return;
            }

            incrementCount();
            LOGGER.debug("Calling getSummonerByName: ");
            c.getSummonerService().getSummonerByName(SUMMONER_TO_LOOK_UP, new Callback<LeagueSummoner>() {
                public void onCompletion(LeagueSummoner summoner) {
                    lock.lock();

                    LOGGER.debug(summoner.getName() + ":");
                    LOGGER.debug("    accountID:  " + summoner.getAccountId());
                    LOGGER.debug("    summonerID: " + summoner.getId());

                    lolUser.setId(summoner.getId());
                    daoCollection.getLolUserDao().insertLolUser(lolUser);

                    decrementCount();
                    lock.unlock();
                }

                public void onError(Exception ex) {
                    lock.lock();
                    ex.printStackTrace();
                    decrementCount();
                    lock.unlock();
                }
            });

        }
        lock.unlock();
        closeAccounts(c);
    }

    private void closeAccounts(LeagueConnection c) {
        for (LeagueAccount account : c.getAccountQueue().getAllAccounts()) {
            LOGGER.debug("Closing account: " + account);
            account.close();
        }
    }
}
