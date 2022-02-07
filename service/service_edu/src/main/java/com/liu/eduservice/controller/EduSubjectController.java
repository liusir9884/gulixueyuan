package com.liu.eduservice.controller;


import com.liu.commonutils.R;
import com.liu.eduservice.entity.subject.OneSubject;
import com.liu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-17
 */
@Api("课程分类")
@RestController
@RequestMapping("/eduservice/subject")

public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    //添加课程分类
    //获取上传过来的文件，把文件内容读取出来
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file)
    {
        //上传过来的文件
        eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();
    }

    //显示课程分类列表(树形)
    @GetMapping("getAllSubject")
    public R getAllSubject()
    {
        //list集合范型是一级分类
        List<OneSubject> list = eduSubjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }



}

