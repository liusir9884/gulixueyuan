package com.liu.eduservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-01-13
 */
public interface EduCommentService extends IService<EduComment> {

    Map<String, Object> getPageComment(Page<EduComment> page, QueryWrapper<EduComment> wrapper);
}
