package com.liu.eduservice.mapper;

import com.liu.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.eduservice.entity.forntVo.CourseFrontInfo;
import com.liu.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-12-20
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getPublishCourseInfo(String courseId);

    public CourseFrontInfo getCourseFrontInfo(String courseId);

    int countCourse(String day);
}
