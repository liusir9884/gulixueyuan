package com.liu.eduservice.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.commonutils.R;
import com.liu.eduservice.entity.EduCourse;
import com.liu.eduservice.entity.EduTeacher;
import com.liu.eduservice.service.EduCourseService;
import com.liu.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")

public class IndexFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduTeacherService eduTeacherService;

    //查询前8条热门课程，查询前4条名师

    @GetMapping("/index")
    public R index(){
        //查询前8条热门课程
        List<EduCourse> eduCourseList = eduCourseService.getHotCourse();
        List<EduTeacher> eduTeacherList = eduTeacherService.getHotTeacher();
        return R.ok().data("courseList",eduCourseList).data("teacherList",eduTeacherList);

    }
}
