package com.liu.oss.controller;

import com.liu.commonutils.R;
import com.liu.oss.service.OssService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api("OSS")
@RestController
@RequestMapping("/eduoss/fileoss")

public class OssController {

    @Autowired
    private OssService ossService;

    //上传图片的方法
    @PostMapping("/upload")
    public R uploadOssFile(MultipartFile file)
    {
        //返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
    //删除图片的方法
    @DeleteMapping("/delete")
    public R deleteOssFile(String url)
    {
        ossService.deleteFileAvater(url);
        return R.ok();
    }
    
}
