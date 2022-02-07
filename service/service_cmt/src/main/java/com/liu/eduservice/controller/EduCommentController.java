package com.liu.eduservice.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.commonutils.JwtUtils;
import com.liu.commonutils.R;
import com.liu.eduservice.client.courseClient;
import com.liu.eduservice.client.userClient;
import com.liu.eduservice.entity.EduComment;
import com.liu.eduservice.entity.UcenterMember;
import com.liu.eduservice.entity.Vo.CommentQuery;
import com.liu.eduservice.entity.Vo.CommentVo;
import com.liu.eduservice.service.EduCommentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/educmt/comment")

public class EduCommentController {

    @Autowired
    private EduCommentService eduCommentService;

    @Autowired
    private userClient user;

    @Autowired
    private courseClient course;

    //前台分页查询课程评论
    @PostMapping("/pageComment/{current}/{limit}")
    public R pageComment(@PathVariable long current, @PathVariable long limit,
                         @RequestBody CommentQuery commentQuery)
    {
        String courseId = commentQuery.getCourseId();
        Page<EduComment> page = new Page<>(current, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        Map<String,Object> map =eduCommentService.getPageComment(page,wrapper);
        return R.ok().data(map);
    }
    //后台分页查询课程评论
    @PostMapping("/pageAdminComment/{current}/{limit}")
    public R pageAdminComment(@PathVariable long current, @PathVariable long limit,
                              @RequestBody CommentVo commentVo)
    {
        Page<EduComment> commentPage = new Page<>(current,limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        String username = commentVo.getUser();
        String coursename = commentVo.getCourse();
        if(!StringUtils.isEmpty(username))
        {
            String userIdByName = user.getUserIdByName(username);
            wrapper.eq("member_id",userIdByName);
        }
        if (!StringUtils.isEmpty(coursename))
        {
            String courseIdByName = course.getCourseIdByName(coursename);
            wrapper.eq("course_id",courseIdByName);
        }
        if(!StringUtils.isEmpty(commentVo.getBegin()))
        {
            //数据库中字段名称
            wrapper.ge("gmt_create",commentVo.getBegin());
        }
        if(!StringUtils.isEmpty(commentVo.getEnd()))
        {
            wrapper.le("gmt_modified",commentVo.getEnd());
        }
        eduCommentService.page(commentPage,wrapper);
        long total = commentPage.getTotal();//总记录数
        List<EduComment> records = commentPage.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //删除评论
    @DeleteMapping("{id}")
    public R deleteComment(@PathVariable String id)
    {
        eduCommentService.removeById(id);
        return R.ok();
    }

    //添加评论
    @PostMapping("/addComment")
    public R addComment(@RequestBody EduComment eduComment, HttpServletRequest request)
    {
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        eduComment.setMemberId(userId);
        UcenterMember member = user.getUserInfoById(userId);
        eduComment.setAvatar(member.getAvatar());
        eduComment.setNickname(member.getNickname());
        eduCommentService.save(eduComment);
        return R.ok();
    }


}
