package com.liu.vod.service.Impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.liu.vod.service.VodService;
import com.liu.vod.utils.ConstantVodUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideo(MultipartFile file) {

        //fileName:上传文件的原始名称
        String fileName=file.getOriginalFilename();
        //title：上传到阿里云上的名称
        String title=fileName.substring(0,fileName.lastIndexOf("."));
        //inputStream:上传文件的输入流
        InputStream inputStream= null;
        try {
            inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId=null;
            videoId=response.getVideoId();
            return videoId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
