package com.liu.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.commonutils.JwtUtils;
import com.liu.commonutils.MD5;
import com.liu.educenter.entity.UcenterMember;
import com.liu.educenter.entity.vo.UcenterQuery;
import com.liu.educenter.mapper.UcenterMemberMapper;
import com.liu.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.servicebase.exceptionhandler.MyException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-01-09
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /**
     *  会员登陆
     * @param ucenterMember
     * @return
     */
    @Override
    public String login(UcenterMember ucenterMember) {
        //获取登陆手机号和密码
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();

        //手机号和密码非空判断
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password))
        {
            throw new MyException(2001,"登陆失败");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember member = baseMapper.selectOne(wrapper);
        if(member==null)
        {
            throw new MyException(2001,"登陆失败");
        }
        if (!MD5.encrypt(password).equals(member.getPassword()))
        {
            throw new MyException(2001,"登陆失败");
        }
        if (member.getIsDisabled())
        {
            throw new MyException(2001,"登陆失败");
        }
        //登陆成功
        //生成token字符串，使用jwt工具
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());


        return jwtToken;
    }

    /**
     * 注册账号
     * @param ucenterQuery
     */
    @Override
    public void register(UcenterQuery ucenterQuery) {

        //获取注册的数据
        //验证码
        String code = ucenterQuery.getCode();
        //手机号
        String mobile = ucenterQuery.getMobile();
        //昵称
        String nickname = ucenterQuery.getNickname();
        //密码
        String password = ucenterQuery.getPassword();

        //手机号和密码非空判断
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)||StringUtils.isEmpty(code)||StringUtils.isEmpty(nickname))
        {
            throw new MyException(2001,"注册失败");
        }
        //判断验证码
        //获取redis的验证码
        String rediscode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(rediscode))
        {
            throw new MyException(2001,"注册失败");
        }
        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer integer = baseMapper.selectCount(wrapper);
        if (integer!=0)
        {
            throw new MyException(2001,"注册失败");
        }
        //将数据加入到数据库
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setNickname(nickname);
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setIsDeleted(false);
        ucenterMember.setAvatar("https://edu-liu1998.oss-cn-beijing.aliyuncs.com/2022/01/08/3e42c9c2dde9454db48498fc4e72beadfile.png");
        baseMapper.insert(ucenterMember);


    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;

    }

    @Override
    public int countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
