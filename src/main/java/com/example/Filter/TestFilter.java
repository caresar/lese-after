package com.example.Filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestFilter.java
 * @Description TODO
 * @createTime 2020年01月15日 14:21:00
 */
@Component
@WebFilter(urlPatterns = "/test/*",filterName = "ssoFilter")
public class TestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        Object account = session.getAttribute("account");
        //得到端口号后面拼接的路径
        String url = request.getRequestURI();
        //放行静态资源
        if(url.endsWith(".css")||url.endsWith(".js")||url.endsWith(".jpg") ||url.endsWith(".gif")||url.endsWith(".png")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (url.equals("/test/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        //微信接口全部放开
        }else if(url.startsWith("/weixin")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }else if (account == null) {
                request.getRequestDispatcher("/test/toLogin").forward(request, response);


        }else{
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
