package com.liu.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.eduservice.entity.forntVo.CourseFrontInfo;
import com.liu.eduservice.entity.forntVo.CourseFrontVo;
import com.liu.eduservice.entity.vo.CourseInfoVo;
import com.liu.eduservice.entity.vo.CoursePublishVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-12-20
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程的基本信息方法
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //课程信息发布方法
    CoursePublishVo publishCourseInfo(String id);

    void deleteCourseById(String id);

    List<EduCourse> getHotCourse();

    Map<String, Object> getCourseFrontList(Page<EduCourse> eduCoursePage, CourseFrontVo courseFrontVo);

    //前台课程详情页面
    CourseFrontInfo getCourseFrontInfo(String courseId);

    int countCourse(String day);
}
