package com.cy.interceptor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @author
 * @version 1.0
 * @data 8:35
 */
@Component
public class CheckTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //放行预检请求
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        //获取token
        String token = request.getHeader("token");
        //校验token合法性
        if (token == null) {
           throw new Exception("token为空,请登录!");
        }
        else {
            // 校验token有效性
            JwtParser parser= Jwts.parser();
            //解析token的SigningKey，并验证token是否有效
            parser.setSigningKey("cy123456");
            //如果token有效，则放行,否则抛出异常
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
        }
        return true;
    }
}
