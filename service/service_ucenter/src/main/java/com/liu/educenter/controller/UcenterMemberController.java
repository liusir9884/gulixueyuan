package com.liu.educenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.commonutils.JwtUtils;
import com.liu.commonutils.R;
import com.liu.educenter.entity.UcenterMember;
import com.liu.educenter.entity.vo.UcenterQuery;
import com.liu.educenter.service.UcenterMemberService;
import com.liu.servicebase.exceptionhandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-01-09
 */
@RestController
@RequestMapping("/educenter/member")

public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登陆
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember ucenterMember)
    {
        //调用service方法实现登陆
        //返回token值，使用jwt
        String token = memberService.login(ucenterMember);
        return R.ok().data("token",token);
    }
    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody UcenterQuery ucenterQuery)
    {
        memberService.register(ucenterQuery);
        return R.ok();
    }
    //根据token获取用户信息
    @GetMapping("getUserInfo")
    public R getUserInfo(HttpServletRequest request)
    {
        //调用jwt工具类的方法，根据request对象获取头像，返回用户id
        String UserId = JwtUtils.getMemberIdByJwtToken(request);
        if(UserId.isEmpty())
        {
            throw new MyException(2001,"登陆失败");
        }
        //查询数据库根据用户id获取用户信息
        UcenterMember member = memberService.getById(UserId);
        return R.ok().data("UserInfo",member);
    }
    //根据用户id获取用户信息
    @GetMapping("getUserInfoById/{id}")
    public UcenterMember getUserInfoById(@PathVariable String id)
    {
        UcenterMember member = memberService.getById(id);
        return member;
    }
    //根据用户名获取用户id
    @PostMapping("/getUserIdByName")
    public String getUserIdByName(@RequestBody String UserName)
    {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname",UserName);
        UcenterMember member = memberService.getOne(wrapper);
        String id = member.getId();
        return id;
    }
    //查询某一天的注册人数
    @GetMapping("countRegister/{day}")
    public int countRegister(@PathVariable String day)
    {
        int count = memberService.countRegisterDay(day);
        return count;
    }


}

