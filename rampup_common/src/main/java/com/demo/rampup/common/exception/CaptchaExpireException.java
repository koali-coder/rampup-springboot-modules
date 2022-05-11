package com.demo.rampup.common.exception;

/**
 * @author zhouyw
 * @date 2022-05-09
 * @describe com.demo.rampup.common.exception
 */
public class CaptchaExpireException extends Exception
{
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException()
    {
        super("user.captcha.exception", null);
    }

    public CaptchaExpireException(String message)
    {
        super(message, null);
    }

}
