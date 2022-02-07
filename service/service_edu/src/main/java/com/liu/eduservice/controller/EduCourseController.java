package com.liu.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.commonutils.R;
import com.liu.eduservice.entity.EduCourse;
import com.liu.eduservice.entity.EduTeacher;
import com.liu.eduservice.entity.vo.CourseInfoVo;
import com.liu.eduservice.entity.vo.CoursePublishVo;
import com.liu.eduservice.entity.vo.CourseQuery;
import com.liu.eduservice.entity.vo.TeacherQuery;
import com.liu.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-20
 */
@Api("课程管理")
@RestController
@RequestMapping("/eduservice/course")

public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //添加课程基本信息的方法
    @ApiOperation("添加课程")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo)
    {
        //添加课程的基本信息
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    //根据课程id查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId)
    {
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }
    //根据课程id得到课程的信息
    @GetMapping("getCoureseInfoOrder/{courseId}")
    public EduCourse getCoureseInfoOrder(@PathVariable String courseId)
    {
        EduCourse course = courseService.getById(courseId);
        return course;
    }
    //课程信息的修改
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo)
    {
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //课程信息发布的显示
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id)
    {
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return R.ok().data("publish",coursePublishVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("/PublishCourse/{id}")
    public R publishCourse(@PathVariable String id)
    {
        EduCourse eduCourse = courseService.getById(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //分页查询功能:current:当前页，limit:每页记录数
    @GetMapping("pageCourse/{current}/{limit}")
    @ApiOperation("分页查询课程")
    public R pageListCourse(@PathVariable("current") long current,@PathVariable("limit") long limit)
    {
        Page<EduCourse> coursePage = new Page<>(current,limit);
        //调用方法实现分页，调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里
        courseService.page(coursePage,null);
        long total = coursePage.getTotal();//总记录数
        List<EduCourse> records = coursePage.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //分页条件查询功能:current:当前页，limit:每页记录数
    @PostMapping("pageCourseCondition/{current}/{limit}")
    @ApiOperation("分页条件查询课程")
    public R pageTeacherCondition(@PathVariable("current") long current,
                                  @PathVariable("limit") long limit,
                                  @RequestBody(required = false) CourseQuery courseQuery)
    {
        Page<EduCourse> page = new Page<>(current,limit);
        //构造条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //多条件组合查询，类似mybatis使用动态sql
        //判断条件值是否为空，如果不空拼接条件
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();

        //判断调节值是否为空
        if(!StringUtils.isEmpty(title))
        {
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status))
        {
            wrapper.eq("status",status);
        }
        //排序
        wrapper.orderByDesc("gmt_modified");
        //调用方法实现分页，调用方法时候，底层封装，把分页所有数据封装到对象里
        courseService.page(page,wrapper);
        long total = page.getTotal();//总记录数
        List<EduCourse> records = page.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }
    //删除课程
    @DeleteMapping("deleteCourse/{id}")
    public R deleteCourse(@PathVariable String id)
    {
        courseService.deleteCourseById(id);
        return R.ok();
    }
    //根据课程名字查询查询课程ID
    @PostMapping("/getCourseIdByName")
    public String getCourseIdByName(@RequestBody String CourseName)
    {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.like("title",CourseName);
        EduCourse course = courseService.getOne(wrapper);
        String id = course.getId();
        return id;
    }
    //查询新增课程数
    @GetMapping("countCourse/{day}")
    public int countCourse(@PathVariable String day)
    {
        int count = courseService.countCourse(day);
        return count;
    }




}

