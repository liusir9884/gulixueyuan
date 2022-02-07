package com.liu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "service-edu")
public interface courseClient {
    //根据课程名字查询查询课程ID
    @PostMapping("/eduservice/course/getCourseIdByName")
    public String getCourseIdByName(@RequestBody String CourseName);
}
