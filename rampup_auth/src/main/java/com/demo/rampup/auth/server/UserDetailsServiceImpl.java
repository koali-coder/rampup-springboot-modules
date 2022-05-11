package com.demo.rampup.auth.server;

import com.demo.rampup.auth.model.LoginUser;
import com.demo.rampup.common.utils.IpUtils;
import com.demo.rampup.common.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhouyw
 * @date 2022-05-09
 * @describe com.demo.rampup.auth.server
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("------用户 {} 身份认证-----",username);

        // 新建用户
        LoginUser user = new LoginUser();
        user.setLoginTime(System.currentTimeMillis());
        user.setIp(IpUtils.getIpAddr(ServletUtils.getRequest()));

        user.setUser(new Object());

        // 设置权限
        Set authoritiesSet = new HashSet();
        // 注意角色权限需要加 ROLE_前缀，否则报403
        GrantedAuthority power = new SimpleGrantedAuthority("ROLE_" + "");
        authoritiesSet.add(power);
        user.setAuthorities(authoritiesSet);

        return user;
    }

}
