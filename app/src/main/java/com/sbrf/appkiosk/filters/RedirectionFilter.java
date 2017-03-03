package com.sbrf.appkiosk.filters;

import com.google.common.base.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class RedirectionFilter implements Filter{
    private static final Logger LOGGER = LogManager.getLogger(RedirectionFilter.class);
    private FilterConfig conf;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        conf = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String message = String.format("------  Request's information:    ServletPath: %s, ContextPath: %s, PathInfo: %s, " +
                        "ServerName: %s, RequestURI: %s, PathTranslated: %s, ContextPath: %s, RemoteUser: %s, Scheme: %s, " +
                        "ServletPath: %s, RequestURL: %s, QueryString: %s",
                request.getServletPath(), request.getContextPath(), request.getPathInfo(),
                request.getServerName(), request.getRequestURI(), request.getPathTranslated(),
                request.getSession().getServletContext().getContextPath(),
                request.getRemoteUser(), request.getScheme(), request.getServletPath(), request.getRequestURL().toString(),
                request.getQueryString());
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug(message);
        }
        System.out.println(message);


        if (request.getScheme().equals("http") || !Strings.isNullOrEmpty(request.getQueryString())) {
            String url = "https://" + request.getServerName() + ":9443";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("!!!!!!!!!! New URL: " + url);
            }
            System.out.println(url);
            response.sendRedirect(url);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
