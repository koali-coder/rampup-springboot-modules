package com.demo.rampup.auth.config;

import com.demo.rampup.auth.filter.JwtTokenFilter;
import com.demo.rampup.auth.handle.DenyHandler;
import com.demo.rampup.auth.handle.ExpAuthenticationEntryPoint;
import com.demo.rampup.auth.handle.OutSuccessHandler;
import com.demo.rampup.auth.server.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author zhouyw
 * @date 2022-05-09
 * @describe com.demo.rampup.auth.config
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DenyHandler denyHandler;

    @Autowired
    OutSuccessHandler outSuccessHandler;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    ExpAuthenticationEntryPoint expAuthenticationEntryPoint;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtTokenFilter authenticationTokenFilter;

    /**
     * @Author lsc
     * <p> 授权</p>
     * @Param [http]
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // swagger授权
                .authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/*").permitAll()
                .antMatchers("/csrf").permitAll()
                .antMatchers("/").permitAll()

                // 匿名用户权限
                .antMatchers("/api/download/**").anonymous()
                //普通用户权限
//                .antMatchers("/api/**").hasRole("USER")
                // 管理员权限
//                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/login", "/captcha").permitAll()
                //其他的需要授权后访问
                .anyRequest().authenticated()
                // 异常
                .and()
                .exceptionHandling()
                //授权异常处理
                .accessDeniedHandler(denyHandler)
                // 认证异常处理
                .authenticationEntryPoint(expAuthenticationEntryPoint)
                .and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(outSuccessHandler)
                .and()
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                // 设置Session的创建策略为：Spring Security不创建HttpSession
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 关闭 csrf 否则post
                .csrf().disable();

    }

    /**
     * @Author lsc
     * <p>认证 设置加密方式 </p>
     * @Param [auth]
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
