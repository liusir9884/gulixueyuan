package com.liu.security.security;

import com.liu.commonutils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//密码处理工具类
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {


    public DefaultPasswordEncoder()
    {
        this(-1);
    }
    public DefaultPasswordEncoder(int strength)
    {

    }

    //进行MD5加密
    @Override
    public String encode(CharSequence charSequence) {
        String s = MD5.encrypt(charSequence.toString());
        return s;
    }

    //密码进行比对
    @Override
    public boolean matches(CharSequence charSequence, String encodePassword) {
        return encodePassword.equals(MD5.encrypt(charSequence.toString()));
    }
}
