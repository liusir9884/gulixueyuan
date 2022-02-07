package com.liu.educms.controller;

import com.liu.commonutils.R;
import com.liu.educms.entity.CrmBanner;
import com.liu.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/educms/bannerFront")

public class BannerFrontController {

    @Autowired
    private CrmBannerService crmBannerService;
    //查询banner,根据id降序排列，显示排列之后的前两条数据
    @GetMapping("getSomeBanner")
    public R getSomeBanner()
    {
        List<CrmBanner> list = crmBannerService.getSomeBanner();
        return R.ok().data("BannerList",list);
    }
}
