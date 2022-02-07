package com.liu.eduservice.controller;


import com.liu.commonutils.R;
import com.liu.eduservice.client.VodClient;
import com.liu.eduservice.entity.EduVideo;
import com.liu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-20
 */
@RestController
@RequestMapping("/eduservice/eduvideo")

public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo)
    {
        videoService.save(eduVideo);
        return R.ok();
    }
    //删除小节
    //同时把阿里云中的视频删除——>通过微服务的方法，调用vod服务中的方法
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId)
    {
        EduVideo eduVideo = videoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();
        if(videoSourceId!=null)
        {
            vodClient.removeAlyVideo(videoSourceId);
        }
        videoService.removeById(videoId);
        return R.ok();
    }
    //修改小节
    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo)
    {
        videoService.updateById(eduVideo);
        return R.ok();
    }
    //根据小节id查询小节
    @GetMapping("/getVideo/{videoId}")
    public R getVideo(@PathVariable String videoId)
    {
        EduVideo eduVideo = videoService.getById(videoId);
        return R.ok().data("eduVideo",eduVideo);
    }


}

