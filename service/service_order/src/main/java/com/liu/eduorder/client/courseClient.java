package com.liu.eduorder.client;

import com.liu.commonutils.R;
import com.liu.eduorder.entity.EduCourse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-edu")
public interface courseClient {
    //根据课程id得到课程的信息
    @GetMapping("/eduservice/course/getCoureseInfoOrder/{courseId}")
    public EduCourse getCoureseInfoOrder(@PathVariable String courseId);
    //根据讲师id查询讲师姓名
    @GetMapping("/eduservice/edu-teacher/getTeacherNameById/{id}")
    public String getTeacherNameById(@PathVariable String id);
}
