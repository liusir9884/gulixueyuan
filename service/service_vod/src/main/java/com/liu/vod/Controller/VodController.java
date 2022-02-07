package com.liu.vod.Controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.liu.commonutils.R;
import com.liu.servicebase.exceptionhandler.MyException;
import com.liu.vod.service.VodService;
import com.liu.vod.utils.ConstantVodUtils;
import com.liu.vod.utils.InitObject;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ListIterator;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    //上传视频到阿里云
    @PostMapping("/uploadVideo")
    public R uploadVideo(MultipartFile file)
    {
        String videoId = vodService.uploadVideo(file);
        return R.ok().data("videoId",videoId);
    }
    //根据视频id删除阿里云中的视频
    @DeleteMapping("/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable String id)
    {
        try {
            //初始化对象
            DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request设置视频id
            request.setVideoIds(id);
            //调用初始化对象方法实现删除
            client.getAcsResponse(request);
            return R.ok();

        }catch (Exception e)
        {
            e.printStackTrace();
            return R.error();
        }
    }
    //删除多个阿里云视频的方法
    //参数为多个视频id
    @DeleteMapping("delete-batch")
    public R deleteVideos(@RequestParam("videoIdList") List<String> videoIdList)
    {
        try {
            //初始化对象
            DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            String str = org.apache.commons.lang.StringUtils.join(videoIdList.toArray(), ",");
            //向request设置视频id
            request.setVideoIds(str);
            //调用初始化对象方法实现删除
            client.getAcsResponse(request);
            return R.ok();

        }catch (Exception e)
        {
            e.printStackTrace();
            return R.error();
        }

    }
    //根据视频id获取视频凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id)
    {
        try {
            //创建初始化对象
            DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取凭证的request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request设置视频id
            request.setVideoId(id);
            //调用方法的搭配凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);


        }catch (Exception e)
        {
            throw new MyException(20001,"获取凭证失败");

        }
    }


}
