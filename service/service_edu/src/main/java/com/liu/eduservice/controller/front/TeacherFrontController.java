package com.liu.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.commonutils.R;
import com.liu.eduservice.entity.EduCourse;
import com.liu.eduservice.entity.EduTeacher;
import com.liu.eduservice.service.EduCourseService;
import com.liu.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.spi.LocaleNameProvider;

@RestController

@RequestMapping("/eduservice/teacherfornt")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService eduTeacherService;
    @Autowired
    private EduCourseService eduCourseService;

    //分页查询讲师条件
    @PostMapping("/getTeacherFront/{page}/{limit}")
    public R getTeacherFront(@PathVariable long page, @PathVariable long limit)
    {
        Page<EduTeacher> eduTeacherPage = new Page<>(page,limit);
        Map<String,Object> map = eduTeacherService.getTeacherFrontList(eduTeacherPage);
        //不用element-ui前端进行分页，所以把数据都返回
        return R.ok().data(map);
    }

    //讲师详情功能
    @GetMapping("/getTeacherInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId)
    {
        //根据讲师id查询讲师基本信息
        EduTeacher teacher = eduTeacherService.getById(teacherId);
        //根据讲师id查询所讲的课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = eduCourseService.list(wrapper);

        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }

}
