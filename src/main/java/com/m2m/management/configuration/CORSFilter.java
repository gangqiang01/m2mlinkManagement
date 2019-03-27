package com.m2m.management.configuration;

import com.alibaba.fastjson.JSONObject;
import com.m2m.management.utils.JwtUtil;
import com.m2m.management.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@Component
@WebFilter(filterName = "CORSFilter")
public class CORSFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CORSFilter.class);
    public static final String headerName = "accesstoken";

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        logger.info("enter");
        HttpServletRequest request = (HttpServletRequest) req;
        String token = request.getHeader(headerName);
        boolean isFilter = false;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, accesstoken,timeout");
        if( request.getRequestURI().equals("/login") || isValid(request, token) || request.getMethod().equals("OPTIONS")){
            chain.doFilter(req, res);
        }else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().format(JSONObject.toJSONString(Response.error("illegal user"))).flush();
            return;
        }
    }

    private boolean isValid(HttpServletRequest req, String token){
        try{
            HttpSession session = req.getSession();
            logger.info(token);
            String username = JwtUtil.getSubject(token);
            logger.info(username);
            String uname = (String)session.getAttribute("username");
            logger.info("uname:"+uname);
            if(uname != null && !username.equals((uname))){
                return false;
            }
            if(username == null){
                return false;
            }

        }catch(Exception e){
            logger.info("jwtError:"+ e);
            return false;
        }

        return true;
    }


    public void init(FilterConfig filterConfig) {}

    public void destroy() {}
}
