package com.liu.educms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.commonutils.R;
import com.liu.educms.entity.CrmBanner;
import com.liu.educms.entity.vo.BannerQuery;
import com.liu.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/educms/bannerAdmin")

public class BannerAdminController {

    @Autowired
    private CrmBannerService crmBannerService;

    @PostMapping("/addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner)
    {
        crmBannerService.save(crmBanner);
        return R.ok();
    }

    @DeleteMapping("/deleteBanner/{id}")
    public R deleteBanner(@PathVariable String id)
    {
        crmBannerService.removeBanner(id);
        return R.ok();
    }
    @PostMapping("/updateBanner")
    public R updateBanner(@RequestBody CrmBanner crmBanner)
    {
        crmBannerService.updateById(crmBanner);
        return R.ok();
    }
    //根据id查询
    @GetMapping("getBanner/{id}")
    public R getBanner(@PathVariable String id)
    {
        CrmBanner banner = crmBannerService.getById(id);
        return R.ok().data("banner",banner);
    }//条件查询
    //分页条件查询功能:current:当前页，limit:每页记录数
    @PostMapping("pageBannerCondition/{current}/{limit}")
    @ApiOperation("分页条件查询幻灯片")
    public R pageTeacherCondition(@PathVariable("current") long current,
                                  @PathVariable("limit") long limit,
                                  @RequestBody(required = false) BannerQuery bannerQuery)
    {
        Page<CrmBanner> page = new Page<>(current,limit);
        //构造条件
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        //多条件组合查询，类似mybatis使用动态sql
        //判断条件值是否为空，如果不空拼接条件
        String title = bannerQuery.getTitle();
        String begin = bannerQuery.getBegin();
        String end = bannerQuery.getEnd();
        //判断调节值是否为空
        if(!StringUtils.isEmpty(title))
        {
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(begin))
        {
            //数据库中字段名称
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end))
        {
            wrapper.le("gmt_modified",end);
        }
        //排序
        wrapper.orderByDesc("gmt_modified");
        //调用方法实现分页，调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里
        crmBannerService.page(page,wrapper);
        long total = page.getTotal();//总记录数
        List<CrmBanner> records = page.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }




}
