package com.lolRiver.river.controllers.fetchers.rawDataFetchers;

import com.lolRiver.river.models.LolUser;

import java.util.List;

/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

public interface RawDataFetcher {
    public void fetchAndStoreRawData(List<LolUser> lolUsers);
}
