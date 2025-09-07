package com.slice.config;


import com.slice.filter.JwtAuthenticationTokenFilter;
import com.slice.pojo.dto.Response;
import com.slice.utils.ServletUtil;
import com.slice.pojo.dto.HttpStatus;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


/**
 * Spring Security 配置类
 * 1、@EnableMethodSecurity 开启方法权限验证
 * 2、@ConfigurationProperties("spring.security") 安全相关配置项位置
 *
 * @author 张康源
 */
@Data
@Configuration
@ConfigurationProperties("spring.security")
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * 可匿名访问的接口路径，不含context-path
     */
    private String[] patterns;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 配置安全相关过滤器链
     *
     * @param http http请求相关安全配置
     * @return 安全过滤器链
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CsrfFilter 禁用CSRF
        http.csrf(AbstractHttpConfigurer::disable);

        // ExceptionTranslationFilter 异常处理器
        http.exceptionHandling(config -> config
                .authenticationEntryPoint((request, response, exception) -> ServletUtil.print(response, Response.error(HttpStatus.FORBIDDEN)))
                .accessDeniedHandler((request, response, exception) -> ServletUtil.print(response, Response.error(HttpStatus.FORBIDDEN)))
        );

        // AuthorizationFilter 授权，除匿名接口外都需要认证
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(patterns).permitAll()
                .requestMatchers("/slices/**").permitAll()
                .anyRequest().authenticated()
        );

        // headers 处理
        http.headers(header -> {
            header.cacheControl(HeadersConfigurer.CacheControlConfig::disable);
            header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
        });

        // TokenFilter 通过 Token 实现认证
        http.addFilterBefore(jwtAuthenticationTokenFilter, AuthorizationFilter.class);

        return http.build();
    }

    /**
     * 配置认证管理器
     *
     * @return 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    /**
     * 配置密码加密器
     *
     * @return 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}