package com.tensquare.user.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, java.lang.Object handler) throws java.lang.Exception {
        System.out.println("经过了拦截器");
        //无论如何都放行，具体能不能操作还是在具体的操作中去判断
        //拦截器只是负责把请求头中包含的token的令牌进行一个解析验证。
        String header=request.getHeader("Authorization");


        if (header!=null&&"".equals(header)){
            //如果包含有Authorization头信息，就对其进行解析
            if (header.startsWith("Bearer")){
                String token=header.substring(7);
                try {
                    Claims claims=jwtUtil.parseJWT(token);
                    String roles= (String) claims.get("roles");
                    if (roles!=null&&roles.equals("admin")){
                        request.setAttribute("claims_admin",token);
                    }
                    if (roles!=null&&roles.equals("user")){
                        request.setAttribute("claims_user",token);
                    }
                }catch (Exception e){
                    throw new RuntimeException("令牌不正确！");
                }
            }
        }
        return true;
    }

}
