package com.demo.rampup.auth.server;

import com.demo.rampup.auth.constant.Constants;
import com.demo.rampup.auth.model.LoginUser;
import com.demo.rampup.common.redis.RedisCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouyw
 * @date 2022-05-09
 * @describe com.demo.rampup.auth.server
 */
@Component
public class TokenServer {

    private static final String CLAIMS_ROLE = "ROLES";

    /**
     * 过期时间
     */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 2;

    /**
     * JWT密码
     */
    private static final String SECRET = "4bbe335135694f35a3cc2a0844a20360";

    private static final String HEADER = "Authorization";

    @Autowired
    private RedisCache redisCache;

    /**
     * 签发JWT
     */
    public String createToken(String username, String roles) {
        String tokenKey = UUID.randomUUID().toString();
        createToken(username, roles, tokenKey);
        return tokenKey;
    }

    /**
     * 签发JWT
     */
    public void createToken(String username, String roles, String tokenKey) {
        Map<String, Object> claims = new HashMap<>(8);
        // 主体
        claims.put(CLAIMS_ROLE, roles);
        String token = Jwts.builder()
                .setClaims(claims)
                .claim("username", username)
                .setExpiration(new Date(Instant.now().toEpochMilli() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        redisCache.setCacheObject(Constants.LOGIN_TOKEN_KEY + tokenKey, token, Long.valueOf(EXPIRATION_TIME).intValue(), TimeUnit.MILLISECONDS);
    }

    /**
     * 验证JWT
     */
    public Boolean validateToken(String token) {
        return (!isTokenExpired(token));
    }

    /**
     * 获取token是否过期
     */
    public Boolean isTokenExpired(String token) {
        Date expiration = getExpireTime(token);
        return expiration.before(new Date());
    }

    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            String username = getUsernameByToken(token);
            return redisCache.getCacheObject(Constants.LOGIN_USER_KEY + username);
        }
        return null;
    }

    /**
     * 根据token获取username
     */
    public String getUsernameByToken(String token) {
        return (String) parseToken(token).get("username");
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String accessToken = request.getHeader(HEADER);
        if (StringUtils.isNotEmpty(accessToken) && accessToken.startsWith(Constants.TOKEN_PREFIX))
        {
            accessToken = accessToken.replace(Constants.TOKEN_PREFIX, "");
        }

        return redisCache.getCacheObject(Constants.LOGIN_TOKEN_KEY + accessToken);
    }

    /**
     * 删除过期token
     *
     * @param request
     * @return token
     */
    public void removeToken(HttpServletRequest request)
    {
        String token = request.getHeader(HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }

        redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + token);
    }

    public Set<GrantedAuthority> getRolseByToken(String token) {
        String rolse = (String) parseToken( token ).get(CLAIMS_ROLE);
        String[] strArray = StringUtils.strip(rolse, "[]").split(", ");
        Set<GrantedAuthority> authoritiesSet = new HashSet();
        if (strArray.length>0){
            Arrays.stream(strArray).forEach(rols-> {
                GrantedAuthority authority = new SimpleGrantedAuthority(rols);
                authoritiesSet.add(authority);
            });
        }
        return authoritiesSet;
    }

    /**
     * 获取token的过期时间
     */
    public Date getExpireTime(String token) {
        Date expiration = parseToken( token ).getExpiration();
        return expiration;
    }

    /**
     * 解析JWT
     */
    private Claims parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

}
