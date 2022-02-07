package com.liu.security.filter;

import com.liu.security.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//授权过滤器
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    public TokenAuthFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate)
    {
        super(authenticationManager);
        this.tokenManager=tokenManager;
        this.redisTemplate=redisTemplate;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取当前认证成功的用户信息
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);
        //判断如果有权限信息，放到权限的上下文中
        if(authRequest!=null)
        {
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        chain.doFilter(request,response);
    }
    public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request)
    {
        //从请求头获取token
        String token = request.getHeader("token");
        //如果token不为空
        if(token!=null)
        {
            //从token获取用户名
            String username = tokenManager.getUserInfoFromToken(token);
            //从redis获取权限列表信息
            List<String> permissionVauleList = (List<String>)redisTemplate.opsForValue().get(username);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for (String s : permissionVauleList) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(s);
                authorities.add(authority);
            }
            return new UsernamePasswordAuthenticationToken(username,token,authorities);

        }
        return null;
    }
}
