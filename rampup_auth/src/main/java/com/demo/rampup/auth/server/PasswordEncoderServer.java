package com.demo.rampup.auth.server;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author zhouyw
 * @date 2022-05-10
 * @describe com.demo.rampup.auth.server
 */
@Component
public class PasswordEncoderServer {

    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static String encodePassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

}
