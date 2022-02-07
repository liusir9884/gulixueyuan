package com.liu.eduservice.service;

import com.liu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-12-20
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeByCourseId(String id);
    void removeByChapterId(String id);
}
