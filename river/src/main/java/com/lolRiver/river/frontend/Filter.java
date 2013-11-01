package com.lolRiver.river.frontend;

import com.lolRiver.river.models.Champion;
import com.lolRiver.river.util.AdaptableHttpServletRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author mxia (mxia@lolRiver.com)
 *         10/9/13
 */

public class Filter implements javax.servlet.Filter {
    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                                     ServletException {
        if (filterConfig == null) {
            return;
        }
        HttpServletRequest req = (HttpServletRequest)request;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        Map<String, String[]> params = req.getParameterMap();

        if (path.startsWith("/static")) {
            chain.doFilter(request, response); // Goes to default servlet.
        } else if (path.equals("/") && params.isEmpty()) {
            request.getRequestDispatcher("/views/?p=1").forward(request, response);
        } else if (path.equals("/searchClips")) {
            AdaptableHttpServletRequest adaptableRequest = new AdaptableHttpServletRequest(req);
            String championName = req.getParameter("championPlayedString");
            adaptableRequest.setParameter("championPlayedString", Champion.Name.readableName(championName));
            request.getRequestDispatcher("/views" + path).forward(adaptableRequest, response);
        } else {
            request.getRequestDispatcher("/views" + path).forward(request, response);
        }
    }
}
