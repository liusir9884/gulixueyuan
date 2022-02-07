package com.liu.staservice.clinet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter")
public interface MemberClient {
    //查询某一天的注册人数
    @GetMapping("/educenter/member/countRegister/{day}")
    public int countRegister(@PathVariable String day);
}
