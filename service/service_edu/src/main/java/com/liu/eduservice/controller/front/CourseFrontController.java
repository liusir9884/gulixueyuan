package com.liu.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.commonutils.JwtUtils;
import com.liu.commonutils.R;
import com.liu.eduservice.client.OrderClient;
import com.liu.eduservice.entity.EduCourse;
import com.liu.eduservice.entity.chapter.ChapterVo;
import com.liu.eduservice.entity.forntVo.CourseFrontInfo;
import com.liu.eduservice.entity.forntVo.CourseFrontVo;
import com.liu.eduservice.service.EduChapterService;
import com.liu.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefornt")
public class CourseFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private OrderClient orderClient;

    //前端条件查询带分页查询
    @PostMapping("/getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody CourseFrontVo courseFrontVo)
    {
        Page<EduCourse> eduCoursePage = new Page<>(page,limit);
        Map<String,Object> map = eduCourseService.getCourseFrontList(eduCoursePage,courseFrontVo);
        return R.ok().data(map);
    }
    //前端课程详情
    @GetMapping("/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request)
    {
        boolean buyCourse=false;
        //根据课程ID，编写sql语句查询课程信息
        CourseFrontInfo courseFrontInfo = eduCourseService.getCourseFrontInfo(courseId);
        //根据课程ID查询章节和小节
        List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(courseId);
        //根据课程ID和用户ID查询当前课程是否已经支付过
        if (!JwtUtils.getMemberIdByJwtToken(request).isEmpty())
        {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            buyCourse = orderClient.isBuyCourse(courseId, memberId);
        }
        return R.ok().data("courseWebVo",courseFrontInfo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);
    }
}
