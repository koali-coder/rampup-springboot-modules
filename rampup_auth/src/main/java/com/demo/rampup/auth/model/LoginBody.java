package com.demo.rampup.auth.model;

import lombok.Data;

/**
 * @author zhouyw
 * @date 2022-05-09
 * @describe com.demo.rampup.auth.model
 */
@Data
public class LoginBody
{
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码标识
     */
    private String random = "";

}
