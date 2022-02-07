package com.liu.eduservice.service;

import com.liu.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-12-17
 */
public interface EduSubjectService extends IService<EduSubject> {

    //添加课程分类
    void saveSubject(MultipartFile file,EduSubjectService eduSubjectService);

    //显示课程分类列表(树形)
    List<OneSubject> getAllOneTwoSubject();
}
