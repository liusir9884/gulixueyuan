package com.liu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.eduservice.entity.EduSubject;
import com.liu.eduservice.entity.excel.ExcelSubjectData;
import com.liu.eduservice.service.EduSubjectService;
import com.liu.servicebase.exceptionhandler.GlobalExceptionHandler;

//不能交给spring管理，需要自己new，不能注入其他对象
public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    public EduSubjectService eduSubjectService;

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    //读取excel内容，一行一行读取
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        if(excelSubjectData!=null)
        {
            //一行一行读取，每次读取有两个值，第一个值一级分类，第二个值二级分类
            //判断一级分类是否重复
            EduSubject oneSubject = this.existOneSubject(eduSubjectService, excelSubjectData.getOneSubjectName());
            if(oneSubject==null)
            {
                oneSubject = new EduSubject();
                oneSubject.setParentId("0");
                oneSubject.setTitle(excelSubjectData.getOneSubjectName());
                eduSubjectService.save(oneSubject);
            }
            String pid = oneSubject.getId();
            EduSubject twoSubject = this.existTwoSubject(eduSubjectService, excelSubjectData.getTwoSubjectName(), pid);
            if(twoSubject==null)
            {
                twoSubject = new EduSubject();
                twoSubject.setParentId(pid);
                twoSubject.setTitle(excelSubjectData.getTwoSubjectName());
                eduSubjectService.save(twoSubject);
            }
        }


    }
    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name)
    {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject onesubject = eduSubjectService.getOne(wrapper);
        return onesubject;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String pid)
    {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twosubject = eduSubjectService.getOne(wrapper);
        return twosubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
