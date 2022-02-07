package com.liu.security.security;

import com.liu.commonutils.R;
import com.liu.commonutils.ResponseUtil;
import com.liu.servicebase.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//退出处理器
public class TokenLogoutHandler implements LogoutHandler {


    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager,RedisTemplate redisTemplate)
    {
        this.tokenManager=tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        //从header里面获取token
        //token不为空，移除token，从redis删除token
        String token = request.getHeader("token");
        if(token!=null)
        {
            //移除
            tokenManager.removeToke(token);
            //从token获取用户名
            String username = tokenManager.getUserInfoFromToken(token);
            redisTemplate.delete(username);
        }
        ResponseUtil.out(response, R.ok());


    }
}
