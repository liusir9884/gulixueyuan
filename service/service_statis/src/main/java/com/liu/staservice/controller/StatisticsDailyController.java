package com.liu.staservice.controller;


import com.liu.commonutils.R;
import com.liu.staservice.clinet.CourseClient;
import com.liu.staservice.clinet.MemberClient;
import com.liu.staservice.entity.StatisticsDaily;
import com.liu.staservice.service.StatisticsDailyService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-01-16
 */
@RestController
@RequestMapping("/staservice/statistics-daily")

public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService dailyService;



    //统计某一天的新增课程和注册人数
    @GetMapping("getStatis/{day}")
    public R getStatis(@PathVariable String day)
    {

        dailyService.addDailyStatis(day);
       return R.ok();
    }

    //图表显示，返回两部分的数据，日期json数组，数量json数组
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,@PathVariable String begin,
                      @PathVariable String end)
    {
        Map<String,Object> map=dailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }


}

