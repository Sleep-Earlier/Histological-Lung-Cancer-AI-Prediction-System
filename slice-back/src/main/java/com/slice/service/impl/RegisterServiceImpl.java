package com.slice.service.impl;

import com.slice.mapper.UserMapper;
import com.slice.pojo.entity.User;
import com.slice.service.RegisterService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public String register(User user) throws Exception {

        User exist = userMapper.queryByUsername(user.getUsername());
        if (exist != null) {
            throw new Exception();
        }
        String encodePassword = passwordEncoder.encode(user.getPassword());
        User newUser = new User(user.getUsername(), user.getName(), encodePassword);
        userMapper.insert(newUser);
        return "成功创建账户";
    }
}
