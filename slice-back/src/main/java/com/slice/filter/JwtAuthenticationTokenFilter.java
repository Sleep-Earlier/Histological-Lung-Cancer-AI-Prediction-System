package com.slice.filter;


import com.slice.mapper.UserMapper;
import com.slice.pojo.entity.User;
import com.slice.service.UserService;
import com.slice.service.impl.UserDetailsImpl;
import com.slice.service.impl.UserServiceImpl;
import com.slice.utils.JwtUtil;
import io.jsonwebtoken.Claims;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;


    @Bean
    public FilterRegistrationBean setBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter( new JwtAuthenticationTokenFilter());
//        bean.addUrlPatterns("/*");
//        System.out.println("注入了 filter");
        return bean;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        // 获取 servlet上下文
        ServletContext sc = request.getSession().getServletContext();
        // 获取 spring 容器
        AbstractApplicationContext cxt = (AbstractApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);
        if(cxt != null && cxt.getBean("userMapper") != null && userMapper == null) {
            // 取出 userInfoService
            userMapper = (UserMapper) cxt.getBean("userMapper");
        }
        String token = request.getHeader("Authorization");

        if (!StringUtils.hasText(token) || !token.startsWith("Token:")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(6);

        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        User user =  userMapper.queryById(Integer.parseInt(userid));
        request.setAttribute("user", user);

        if (user == null) {
            throw new RuntimeException("用户未登录");
        }

        UserDetailsImpl loginUser = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
