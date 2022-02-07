package com.liu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.eduservice.client.VodClient;
import com.liu.eduservice.entity.EduVideo;
import com.liu.eduservice.mapper.EduVideoMapper;
import com.liu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-20
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;
    @Override
    public void removeByCourseId(String id) {
        //根据课程id查询课程所有的视频id
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapper);
        ArrayList<String> videoList = new ArrayList<>();
        for (EduVideo eduVideo : eduVideoList) {
            String videoSourceId = eduVideo.getVideoSourceId();
            if (videoSourceId!=null)
            {
                videoList.add(videoSourceId);
            }
        }
        if (videoList.size()>0)
        {
            vodClient.deleteVideos(videoList);
        }
        baseMapper.delete(wrapper);
    }

    @Override
    public void removeByChapterId(String id) {
        //根据章节id查询课程所有的视频id
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",id);
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapper);
        ArrayList<String> videoList = new ArrayList<>();
        for (EduVideo eduVideo : eduVideoList) {
            String videoSourceId = eduVideo.getVideoSourceId();
            if (videoSourceId!=null)
            {
                videoList.add(videoSourceId);
            }
        }
        if (videoList.size()>0)
        {
            vodClient.deleteVideos(videoList);
        }
        baseMapper.delete(wrapper);

    }
}
