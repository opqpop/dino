package com.lolRiver.achimala.leaguelib.connection;

import com.lolRiver.achimala.leaguelib.errors.LeagueException;
import org.apache.log4j.Logger;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/7/13
 */

public class AccountConnectorRunnable implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(AccountConnectorRunnable.class.getName());

    private LeagueConnection leagueConnection;
    private LeagueAccount leagueAccount;

    public AccountConnectorRunnable(LeagueConnection leagueConnection, LeagueAccount leagueAccount) {
        this.leagueConnection = leagueConnection;
        this.leagueAccount = leagueAccount;
    }

    @Override
    public void run() {
        try {
            leagueConnection.getAccountQueue().addAccount(leagueAccount);
        } catch (LeagueException e) {
            LOGGER.error("Couldn't connect account: " + leagueAccount, e);
        }
    }
}
