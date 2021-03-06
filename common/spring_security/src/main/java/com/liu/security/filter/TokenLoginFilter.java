package com.liu.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liu.commonutils.R;
import com.liu.commonutils.ResponseUtil;
import com.liu.security.entity.SecurityUser;
import com.liu.security.entity.User;
import com.liu.security.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

//认证过滤器
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/acl/login","POST"));
    }

    //获取表单提交的用户名和密码
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        //获取表单提交的数据
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            //权限
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),new ArrayList<>()));
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException();
        }


    }
    //认证成功调用的方法
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException
    {
        //认证成功，得到认证成功之后的用户信息
        SecurityUser principal = (SecurityUser)authResult.getPrincipal();
        //根据用户名生成token
        String token = tokenManager.createToken(principal.getCurrentUserInfo().getUsername());
        //把用户名和用户权限列表放到redis
        redisTemplate.opsForValue().set(principal.getCurrentUserInfo().getUsername(),principal.getPermissionValueList());
        //返回token
        ResponseUtil.out(response, R.ok().data("token",token));
    }
    //认证失败调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException
    {
        //返回token
        ResponseUtil.out(response, R.error());
    }
}

