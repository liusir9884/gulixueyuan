package com.liu.edumsm.controller;

import com.liu.commonutils.R;
import com.liu.edumsm.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/edumsm/msm")

public class MsmController {
    @Autowired
    private MsmService msmService;



    @GetMapping("send/{phone}")
    public R sendSms(@PathVariable String phone){
        //调用service发送短信的方法
        boolean isSend = msmService.send(phone);
        if (isSend){
            return R.ok();
        }else {
            return R.error().message("短信发送失败！");
        }
    }
}
