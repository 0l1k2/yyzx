package com.cy.interceptor;
import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cy.utils.Jwt;
/**
 * @author
 * @version 1.0
 * @data 8:35
 */
@Component
public class CheckTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private Jwt jwt;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //放行预检请求
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }
        //token验证
        String token = request.getHeader(jwt.getHeader());
        //检查token字符串是否为空字符串或长度为0
        if(StringUtils.isEmpty(token)) {
            token=request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)){
            throw new SignatureException(jwt.getHeader() + " token 不能为空");
        }
        Claims claims = null;
        try{
            claims=jwt.getTokenClaims(token);
            if (claims == null||jwt.isTokenExpired(claims.getExpiration())){
                throw new SignatureException(jwt.getHeader() + " token 已过期,请重新登录");
            }}catch (Exception e){
            throw new SignatureException(jwt.getHeader() + " token 已过期,请重新登录");
        }
        request.setAttribute("claims", claims.getSubject());
        return true;
    }
}
