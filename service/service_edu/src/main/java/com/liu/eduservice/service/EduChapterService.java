package com.liu.eduservice.service;

import com.liu.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-12-20
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    void deleteChapter(String chapterId);

    void removeByCourseId(String id);
}
