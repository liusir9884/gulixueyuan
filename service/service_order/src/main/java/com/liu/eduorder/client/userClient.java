package com.liu.eduorder.client;

import com.liu.eduorder.entity.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter")
public interface userClient {

    //根据用户id获取用户信息
    @GetMapping("/educenter/member/getUserInfoById/{id}")
    public UcenterMember getUserInfoById(@PathVariable String id);
}
