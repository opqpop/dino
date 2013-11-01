package com.lolRiver.river.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class AdaptableHttpServletRequest extends HttpServletRequestWrapper {
    private Map<String, String> params = new HashMap();

    public AdaptableHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        // if we added one, return that one
        if (params.get(name) != null) {
            return params.get(name);
        }
        // otherwise return what's in the original request
        HttpServletRequest req = (HttpServletRequest)super.getRequest();
        return req.getParameter(name);
    }

    public void setParameter(String name, String value) {
        params.put(name, value);
    }
}
