package com.patrick.disruptoromssettlement.filter;

import com.google.common.collect.Sets;
import com.patrick.disruptoromssettlement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SessionCheckFilter implements Filter {

    @Autowired
    private AccountService accountService;
    //不需要身份验证的请求路径
    private Set<String> whiteRootPaths = Sets.newHashSet(
            "login","msgsocket","test","api"
    );
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //解决跨域问题
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI(); //  /login/pwdsetting
        String[] split = path.split("/");
        if(split.length < 2) {
            request.getRequestDispatcher("/login/loginfail").forward(servletRequest,servletResponse);
        } else {
            if(!whiteRootPaths.contains(split[1])){
                //不在白名单 验证token
                if(accountService.accountExistInCache(
                        request.getParameter("token")
                )){
                    filterChain.doFilter(servletRequest, servletResponse);
                }else {
                    request.getRequestDispatcher(
                            "/login/loginfail"
                    ).forward(servletRequest, servletResponse);
                }
            }else {
                //在白名单 放行
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }

        //放行请求
        //filterChain.doFilter(servletRequest, servletResponse);
     }
}
