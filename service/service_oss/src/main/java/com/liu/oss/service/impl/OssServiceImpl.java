package com.liu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.liu.oss.service.OssService;
import com.liu.oss.util.ConstantPropertiesUtils;
import org.apache.poi.util.StringUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像到oss
    @Override
    public String uploadFileAvatar(MultipartFile file){

        // yourEndpoint填写Bucket所在地域对应的Endpoint。
        // 以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件的输入流
            InputStream inputStream = file.getInputStream();

            //获取上传文件名称
            String filename = file.getOriginalFilename();

            //在文件名称里面添加随机唯一的值,防止上传相同的文件被覆盖
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            filename=uuid+filename;

            //把文件按照日期进行分类
            //获取当前的日期
            String path = new DateTime().toString("yyyy/MM/dd");
            filename=path+"/"+filename;

            //调用oss方法实现上传
            //第一个参数 bucket名称 第二个参数上传到oss文件路径和名称,第三个参数文件输入流
            ossClient.putObject(bucketName, filename, inputStream);


            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径放回
            //需要把上传到阿里云oss路径手动拼接出来
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteFileAvater(String url) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。
        // 以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String fileurl= url.substring(48);
        System.out.println(fileurl);

        ossClient.deleteObject(bucketName, fileurl);

        // 关闭OSSClient。
        ossClient.shutdown();

    }
}
