package com.liu.staservice.schedule;

import com.liu.staservice.service.StatisticsDailyService;
import com.liu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;

    //在每天凌晨1点，把前一天数据进行数据查询添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        dailyService.addDailyStatis(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }
}
