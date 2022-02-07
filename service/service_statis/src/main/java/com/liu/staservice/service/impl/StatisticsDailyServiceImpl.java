package com.liu.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.staservice.clinet.CourseClient;
import com.liu.staservice.clinet.MemberClient;
import com.liu.staservice.entity.StatisticsDaily;
import com.liu.staservice.mapper.StatisticsDailyMapper;
import com.liu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-01-16
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private MemberClient memberClient;

    @Override
    public void addDailyStatis(String day) {
        //添加记录之前先删除表相同的日期
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        int courseCount = courseClient.countCourse(day);
        int memberCount = memberClient.countRegister(day);
        StatisticsDaily daily = new StatisticsDaily();
        daily.setCourseNum(courseCount);
        daily.setRegisterNum(memberCount);
        daily.setDateCalculated(day);
        daily.setLoginNum(RandomUtils.nextInt(100,200));
        daily.setVideoViewNum(RandomUtils.nextInt(100,200));
        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);
        //因为返回有两部分内容且要数组形式，我们要使用到json数组形式，而在后端中集合会变成数组形式。map和对象会变成json对象形式
        ArrayList<String> date = new ArrayList<>();
        ArrayList<Integer> number = new ArrayList<>();
        //遍历查询所有数据，进行封装
        for (StatisticsDaily daily : staList) {
            date.add(daily.getDateCalculated());
            //封装数量
            switch (type)
            {
                case "login_num":
                    number.add(daily.getLoginNum());
                    break;
                case "register_num":
                    number.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    number.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    number.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("date_calculatedList",date);
        map.put("numDataList",number);
        return map;
    }
}
