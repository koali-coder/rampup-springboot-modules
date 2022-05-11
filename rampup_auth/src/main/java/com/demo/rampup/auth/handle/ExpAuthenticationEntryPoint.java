package com.demo.rampup.auth.handle;

import com.demo.rampup.common.model.RestData;
import com.google.gson.Gson;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhouyw
 * @date 2022-05-09
 * @describe com.demo.rampup.auth.handle
 */
@Component
public class ExpAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 设置响应头
        httpServletResponse.setContentType("application/json;charset=utf-8");
        // 返回值
        RestData result = RestData.failed();
        httpServletResponse.getWriter().write(new Gson().toJson(result));
    }

}
