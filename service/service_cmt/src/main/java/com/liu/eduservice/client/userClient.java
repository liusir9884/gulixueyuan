package com.liu.eduservice.client;

import com.liu.commonutils.R;
import com.liu.eduservice.entity.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient(name = "service-ucenter")
public interface userClient {

    @GetMapping("/educenter/member/getUserInfoById/{id}")
    public UcenterMember getUserInfoById(@PathVariable String id);

    //根据用户名获取用户id
    @PostMapping("/educenter/member/getUserIdByName")
    public String getUserIdByName(@RequestBody String UserName);


}
