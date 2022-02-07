package com.liu.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface OssService {
    //上传头像到oss
    String uploadFileAvatar(MultipartFile file);

    void deleteFileAvater(String url);
}
