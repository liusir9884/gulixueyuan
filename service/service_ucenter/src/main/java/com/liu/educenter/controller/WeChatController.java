package com.liu.educenter.controller;

import com.google.gson.Gson;
import com.liu.commonutils.JwtUtils;
import com.liu.educenter.entity.UcenterMember;
import com.liu.educenter.service.UcenterMemberService;
import com.liu.educenter.utils.HttpClientUtils;
import com.liu.educenter.utils.WeChatUtils;
import com.liu.servicebase.exceptionhandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WeChatController {

    @Autowired
    private UcenterMemberService ucenterMemberService;


    //1.生成微信扫描二维码
    @GetMapping("/login")
    public String getWeChatCode()
    {
        //微信开放平台授权baseUrl，%s相当于？代表占位符
        String baseurl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对redirect_url进行URLEncode编码
        String redirectUrl = WeChatUtils.WX_OPEN_REDIRECT_URL;

        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置%s的这些值
        String url = String.format(
                baseurl,
                WeChatUtils.WX_OPEN_APP_ID,
                WeChatUtils.WX_OPEN_REDIRECT_URL,
                "atguigu"
        );
        //请重定向求微信地址
        return "redirect:"+url;
    }
    //2.获得扫码人的信息，添加数据
    @GetMapping("callback")
    public String callback(String code,String state)
    {

        //获取code值，临时票据，类似于二维码
        //拿着code请求微信固定地址，得到两个值accsess_token和openid
        try {
            String baseAccessTokenUrl  = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                                        "?appid=%s" +
                                        "&secret=%s" +
                                        "&code=%s" +
                                        "&grant_type=authorization_code";
            //拼接三个参数：id 密钥和code值
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    WeChatUtils.WX_OPEN_APP_ID,
                    WeChatUtils.WX_OPEN_APP_SECRET,
                    code);
            //请求这个拼接好的地址，得到返回两个值accsess_token和openid
            //使用httpclient发送请求，得到返回的结果，不用浏览器也可以有请求和响应
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //从accessTokenInfo中拿出来accsess_token和openid
            //把accessTokenInfo转换成map集合，根据map里面key获取对应值
            Gson gson = new Gson();
            HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
            String accessToken = (String)map.get("access_token");
            String openid = (String)map.get("openid");


            //把扫码人信息添加到数据库中
            //判断数据库表里是否存在相同信息->根据openid查询
            UcenterMember member = ucenterMemberService.getOpenIdMember(openid);
            if (member==null){
                //拿着accsess_token和openid再去请求一个固定地址，获取到扫描人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回userInfo字符串扫描人的信息
                HashMap usermap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) usermap.get("nickname");//昵称
                String headimgurl = (String)usermap.get("headimgurl");//头像

                //如果member为空，表里没有相同的数据，进行添加
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                ucenterMemberService.save(member);

            }
            //使用jwt根据member对象生成字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            return "redirect:http://localhost:3000?token="+jwtToken;
        } catch (Exception e) {
            throw new MyException(20001,"登陆失败");
        }

    }
}
