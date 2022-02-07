package com.liu.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.commonutils.R;
import com.liu.eduservice.entity.EduCourse;
import com.liu.eduservice.entity.EduTeacher;
import com.liu.eduservice.entity.vo.TeacherQuery;
import com.liu.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-11
 */
@Api("讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")

public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    //1.查询讲师表所有数据
    //rest风格,浏览器有些无法测试
    @ApiOperation("讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher()
    {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    //逻辑删除讲师的方法
    @DeleteMapping("{id}")
    @ApiOperation("逻辑删除讲师")
    public R removeTeacher(@PathVariable("id") String id)
    {
        boolean flag = eduTeacherService.removeById(id);
        if(flag==true)
        {
            return R.ok();
        }
        return R.error();
    }

    //分页查询功能:current:当前页，limit:每页记录数
    @GetMapping("pageTeacher/{current}/{limit}")
    @ApiOperation("分页查询讲师")
    public R pageListTeacher(@PathVariable("current") long current,@PathVariable("limit") long limit)
    {
        Page<EduTeacher> teacherPage = new Page<>(current,limit);
        //调用方法实现分页，调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里
        eduTeacherService.page(teacherPage,null);
        long total = teacherPage.getTotal();//总记录数
        List<EduTeacher> records = teacherPage.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //分页条件查询功能:current:当前页，limit:每页记录数
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    @ApiOperation("分页条件查询讲师")
    public R pageTeacherCondition(@PathVariable("current") long current,
                                  @PathVariable("limit") long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery)
    {
        Page<EduTeacher> page = new Page<>(current,limit);
        //构造条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询，类似mybatis使用动态sql
        //判断条件值是否为空，如果不空拼接条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断调节值是否为空
        if(!StringUtils.isEmpty(name))
        {
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level))
        {
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin))
        {
            //数据库中字段名称
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end))
        {
            wrapper.le("gmt_modified",end);
        }
        //排序
        wrapper.orderByDesc("gmt_modified");
        //调用方法实现分页，调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里
        eduTeacherService.page(page,wrapper);
        long total = page.getTotal();//总记录数
        List<EduTeacher> records = page.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //添加讲师接口方法
    @PostMapping("addTeacher")
    @ApiOperation("添加讲师")
    public R addTeacher(@RequestBody EduTeacher eduTeacher)
    {
        boolean save = eduTeacherService.save(eduTeacher);
        if(save)
        {
            return R.ok();
        }
        return R.error();
    }

    //根据讲师id进行查询
    @GetMapping("getTeacher/{id}")
    @ApiOperation("根据讲师id进行查询")
    public R getTeacher(@PathVariable String id)
    {
        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    //修改讲师
    @PostMapping("updateTeacher")
    @ApiOperation("修改讲师")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher)
    {
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if(flag)
        {
            return R.ok();
        }
        return R.error();
    }
    //根据讲师id查询讲师姓名
    @GetMapping("getTeacherNameById/{id}")
    public String getTeacherNameById(@PathVariable String id)
    {
        EduTeacher teacher = eduTeacherService.getById(id);
        String teacherName = teacher.getName();
        return teacherName;
    }



}

