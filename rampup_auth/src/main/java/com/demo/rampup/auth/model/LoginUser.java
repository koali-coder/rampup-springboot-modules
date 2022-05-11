package com.demo.rampup.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * @author zhouyw
 * @date 2022-05-09
 * @describe com.demo.rampup.auth.model
 */
@Data
public class LoginUser implements UserDetails
{
    private static final long serialVersionUID = 1L;

    /**
     * 登陆时间
     */
    private Long loginTime;

    /**
     * 登录IP地址
     */
    private String ip;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 权限信息
     */
    private Set<? extends GrantedAuthority> authorities;

    /**
     * 用户信息
     */
    private Object user;

    public LoginUser()
    {
    }

    public LoginUser(Object user, Set<? extends GrantedAuthority> authorities)
    {
        this.user = user;
        this.authorities = authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword()
    {
//        return user.getPassword();
        return null;
    }

    @Override
    public String getUsername()
    {
//        return user.getUsername();
        return null;
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isEnabled()
    {
        return true;
    }

}
