package com.demo.rampup.auth.server;

import com.demo.rampup.auth.constant.Constants;
import com.demo.rampup.auth.model.LoginUser;
import com.demo.rampup.common.exception.CaptchaExpireException;
import com.demo.rampup.common.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author zhouyw
 * @date 2022-05-09
 * @describe com.demo.rampup.auth.server
 */
@Slf4j
@Component
public class LoginServer {

    @Autowired
    private TokenServer tokenServer;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param random 验证码标识
     * @return 结果
     */
    public String login(String username, String password, String code, String random) throws CaptchaExpireException {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + random;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (StringUtils.isEmpty(captcha)) {
            throw new CaptchaExpireException("请输入验证码");
        } else if (!code.equals(captcha)) {
            throw new CaptchaExpireException("验证码错误");
        }

        // 用户验证
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        redisCache.setCacheObject(Constants.LOGIN_USER_KEY + username, loginUser);
        // 生成token
        return tokenServer.createToken(username, loginUser.getAuthorities().toString());
    }

    /**
     * 无验证码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String login(String username, String password) {
        // 用户验证
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        redisCache.setCacheObject(Constants.LOGIN_USER_KEY + username, loginUser);
        // 生成token
        return tokenServer.createToken(username, loginUser.getAuthorities().toString());
    }

}
