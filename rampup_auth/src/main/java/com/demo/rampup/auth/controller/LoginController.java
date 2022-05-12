package com.demo.rampup.auth.controller;

import com.demo.rampup.auth.constant.Constants;
import com.demo.rampup.auth.model.LoginBody;
import com.demo.rampup.auth.model.LoginUser;
import com.demo.rampup.auth.server.LoginServer;
import com.demo.rampup.auth.server.TokenServer;
import com.demo.rampup.auth.util.PasswordCryptUtil;
import com.demo.rampup.common.model.RestData;
import com.demo.rampup.common.redis.RedisCache;
import com.demo.rampup.common.utils.ServletUtils;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouyw
 * @date 2022-05-10
 * @describe com.demo.rampup.auth.controller
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    LoginServer loginServer;

    @Autowired
    RedisCache redisCache;

    @Autowired
    Producer producer;

    @Autowired
    TokenServer tokenServer;

    /**
     * 登录方法
     * @return 结果
     */
    @PostMapping("/login")
    public RestData login(@RequestBody LoginBody loginBody) {
        try {
            String password = PasswordCryptUtil.desEncrypt(loginBody.getPassword());
            String token = loginServer.login(loginBody.getUsername(), loginBody.getPassword(),
                    loginBody.getCode(), loginBody.getRandom());
            return RestData.success(token);
        } catch (BadCredentialsException e) {
            return RestData.failed("密码错误");
        } catch (InternalAuthenticationServiceException e) {
            return RestData.failed("用户不存在");
        }catch (Exception e) {
            log.error("login error ", e);
            return RestData.failed(e.getMessage());
        }
    }

    @GetMapping("/captcha")
    public RestData<Object> captcha(@RequestParam("random") String random) throws IOException {
        String code = producer.createText();
        //转换为64位编码
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());

        //存入Redis中
        redisCache.setCacheObject(Constants.CAPTCHA_CODE_KEY + random, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        log.info("captcha  {}  {}", random, code);
        return RestData.success(base64Img);
    }

    @GetMapping("/userInfo")
    public RestData<Object> userInfo() {
        LoginUser loginUser = tokenServer.getLoginUser(ServletUtils.getRequest());
        return RestData.success(loginUser);
    }

}
