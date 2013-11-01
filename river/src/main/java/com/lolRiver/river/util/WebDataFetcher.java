package com.lolRiver.river.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/28/13
 */

@Component
public class WebDataFetcher {
    public String get(String url) throws Exception {
        return get(url, null, null);
    }

    public String get(String url, String headerName, String headerValue) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        if (StringUtils.isNotBlank(headerName) && StringUtils.isNotBlank(headerValue)) {
            request.setHeader(headerName, headerValue);
        }
        HttpResponse response = client.execute(request);

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
