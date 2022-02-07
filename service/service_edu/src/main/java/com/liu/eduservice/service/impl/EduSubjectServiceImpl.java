package com.liu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.eduservice.entity.EduSubject;
import com.liu.eduservice.entity.excel.ExcelSubjectData;
import com.liu.eduservice.entity.subject.OneSubject;
import com.liu.eduservice.entity.subject.TwoSubject;
import com.liu.eduservice.entity.vo.TeacherQuery;
import com.liu.eduservice.listener.SubjectExcelListener;
import com.liu.eduservice.mapper.EduSubjectMapper;
import com.liu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-17
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {
        try {
            //文件输入流
            InputStream inputStream = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(inputStream, ExcelSubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //显示课程分类列表(树形)
    @Override
    public List<OneSubject> getAllOneTwoSubject() {

        ArrayList<OneSubject> list = new ArrayList<>();
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        //service调取mapper
        List<EduSubject> oneSubjects = baseMapper.selectList(wrapperOne);
        for (EduSubject oneSubject : oneSubjects) {
            QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
            wrapperTwo.eq("parent_id",oneSubject.getId());
            OneSubject subject = new OneSubject();
            subject.setId(oneSubject.getId());
            subject.setTitle(oneSubject.getTitle());
            List<EduSubject> twoSubjects = baseMapper.selectList(wrapperTwo);
            ArrayList<TwoSubject> children = new ArrayList<>();
            for (EduSubject twoSubject : twoSubjects) {
                TwoSubject twoSubject1 = new TwoSubject();
                twoSubject1.setId(twoSubject.getId());
                twoSubject1.setTitle(twoSubject.getTitle());
                children.add(twoSubject1);
            }
            subject.setChildren(children);
            list.add(subject);
        }
        return list;
    }


}
