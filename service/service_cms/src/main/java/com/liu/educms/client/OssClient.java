package com.liu.educms.client;

import com.liu.commonutils.R;
import org.apache.ibatis.annotations.Delete;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Component
@FeignClient(name = "service-oss")
public interface OssClient {

    //上传图片的方法
    @PostMapping("/eduoss/fileoss/upload")
    public R uploadOssFile(MultipartFile file);

    //删除图片的方法
    @DeleteMapping("/eduoss/fileoss/delete")
    public R deleteOssFile(@RequestParam("url") String url);

}
