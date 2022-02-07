package com.liu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.eduservice.entity.EduChapter;
import com.liu.eduservice.entity.EduVideo;
import com.liu.eduservice.entity.chapter.ChapterVo;
import com.liu.eduservice.entity.chapter.VideoVo;
import com.liu.eduservice.mapper.EduChapterMapper;
import com.liu.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-20
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduVideoService eduVideoService;
    //课程大纲列表，根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //1.查询课程章节的信息
        QueryWrapper<EduChapter> wrapperone = new QueryWrapper<>();
        QueryWrapper<EduVideo> wrappertwo = new QueryWrapper<>();
        wrapperone.eq("course_id",courseId);
        wrappertwo.eq("course_id",courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(wrapperone);
        List<EduVideo> lists = eduVideoService.list(wrappertwo);
        ArrayList<ChapterVo> chapterVos = new ArrayList<>();
        for (EduChapter eduChapter : eduChapters) {
            ArrayList<VideoVo> arrayList = new ArrayList<>();
            ChapterVo chapterVo = new ChapterVo();
            chapterVo.setId(eduChapter.getId());
            chapterVo.setTitle(eduChapter.getTitle());
            for (EduVideo list : lists)
            {
                if (list.getChapterId().equals(eduChapter.getId()))
                {
                    VideoVo videoVo = new VideoVo();
                    videoVo.setId(list.getId());
                    videoVo.setTitle(list.getTitle());
                    videoVo.setVideoSourceId(list.getVideoSourceId());
                    arrayList.add(videoVo);
                }
            }
            chapterVo.setChildren(arrayList);
            chapterVos.add(chapterVo);
        }
        return chapterVos;



    }

    //删除章节和小节
    @Override
    public void deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        if (eduVideoService.getOne(wrapper)!=null)
        {

            eduVideoService.removeByChapterId(chapterId);
        }
        baseMapper.deleteById(chapterId);

    }

    //删除章节
    @Override
    public void removeByCourseId(String id) {

        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        baseMapper.delete(wrapper);
    }
}
