package com.slice.service.impl;

import com.slice.pojo.dto.HttpStatus;
import com.slice.pojo.entity.User;
import com.slice.service.LoginService;
import com.slice.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Override
    public String login(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // 将封装好的authenticationToken进行验证，判断是否合法，非法会自动报异常
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            // 若合法，则将其取出并赋予用户UserDetailsImpl类中的各种属性，形成loginUser
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();

            // 单独取loginUser中的用户信息，用于生成JWT-token
            User user = loginUser.getUser();
            String jwt = JwtUtil.createJWT(user.getId().toString());

            return jwt;
        } catch (Exception e) {
            throw e;
        }
    }
}
