package com.cy.interceptor;
import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cy.utils.Jwt;
import org.slf4j.Logger;
import java.io.IOException;
import org.slf4j.LoggerFactory;

/**
 * @author
 * @version 1.0
 * @data 8:35
 */
@Component
public class CheckTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private Jwt jwt;



//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//
//        //放行预检请求
//        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
//            return true;
//        }
//        //token验证
//        String token = request.getHeader(jwt.getHeader());
//        //检查token字符串是否为空字符串或长度为0
//        if(StringUtils.isEmpty(token)) {
//            token=request.getParameter("token");
//        }
//        if (StringUtils.isEmpty(token)){
//            throw new SignatureException(jwt.getHeader() + " token 不能为空");
//        }
//        Claims claims = null;
//        try{
//            claims=jwt.getTokenClaims(token);
//            if (claims == null||jwt.isTokenExpired(claims.getExpiration())){
//                throw new SignatureException(jwt.getHeader() + " token 已过期,请重新登录");
//            }}catch (Exception e){
//            throw new SignatureException(jwt.getHeader() + " token 已过期,请重新登录");
//        }
//        request.setAttribute("claims", claims.getSubject());
//        return true;
//    }
//}

    private static final Logger log = LoggerFactory.getLogger(CheckTokenInterceptor.class);

    @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // 放行预检请求
            if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
                return true;
            }

            // 记录请求信息
            log.info("Handling request: {} {}", request.getMethod(), request.getRequestURI());

            // Token验证
            String token = request.getHeader(jwt.getHeader());
            log.debug("Extracted token from header: {}", token);

            if (StringUtils.isEmpty(token)) {
                token = request.getParameter("token");
                log.debug("Extracted token from parameter: {}", token);
            }

            if (StringUtils.isEmpty(token)) {
                log.warn("Token is missing in request: {}", request.getRequestURI());
                handleTokenError(response, "token 不能为空");
                return false;
            }

            try {
                Claims claims = jwt.getTokenClaims(token);
                if (claims == null || jwt.isTokenExpired(claims.getExpiration())) {
                    log.warn("Invalid or expired token for request: {}", request.getRequestURI());
                    handleTokenError(response, "token 已过期,请重新登录");
                    return false;
                }

                request.setAttribute("claims", claims.getSubject());
                log.debug("Token validated successfully for user: {}", claims.getSubject());
                return true;
            } catch (Exception e) {
                log.error("Token validation failed for request: {}", request.getRequestURI(), e);
                handleTokenError(response, "token 验证失败,请重新登录");
                return false;
            }
        }

        private void handleTokenError(HttpServletResponse response, String message) throws IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\"}");
        }
    }