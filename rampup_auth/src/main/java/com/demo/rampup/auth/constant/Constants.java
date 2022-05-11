package com.demo.rampup.auth.constant;

/**
 * @author zhouyw
 * @date 2022-05-09
 * @describe com.demo.rampup.auth.constant
 */
public class Constants
{

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "nepcs_captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "nepcs_login_tokens:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "nepcs_login_user_key:";

}
