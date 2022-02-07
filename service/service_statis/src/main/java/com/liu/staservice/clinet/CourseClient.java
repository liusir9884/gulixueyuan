package com.liu.staservice.clinet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-edu")
public interface CourseClient {

    //查询新增课程数
    @GetMapping("/eduservice/course/countCourse/{day}")
    public int countCourse(@PathVariable String day);
}
