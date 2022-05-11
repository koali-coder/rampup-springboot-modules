package com.demo.rampup.auth.handle;

import com.demo.rampup.common.model.RestData;
import com.google.gson.Gson;
import com.demo.rampup.auth.server.TokenServer;
import com.demo.rampup.common.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
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
public class OutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    TokenServer tokenServer;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException, IOException {
        // 设置响应头
        httpServletResponse.setContentType("application/json;charset=utf-8");
        // 删除token
        tokenServer.removeToken(ServletUtils.getRequest());
        // 返回值
        RestData result = RestData.success("退出登陆成功");
        httpServletResponse.getWriter().write(new Gson().toJson(result));
    }

}
