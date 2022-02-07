package com.liu.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.commonutils.JwtUtils;
import com.liu.commonutils.R;
import com.liu.eduorder.entity.TOrder;
import com.liu.eduorder.entity.vo.OrderQuery;
import com.liu.eduorder.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/eduorder/t-order")

public class TOrderController {

    @Autowired
    private TOrderService tOrderService;

    //生成订单的方法
    @PostMapping("/createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request)
    {
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        String orderNo = tOrderService.createOrders(courseId,userId);
        return R.ok().data("orderNo",orderNo);
    }
    //根据订单id查询订单信息
    @GetMapping("/getOderInfo/{orderNo}")
    public R getOderInfo(@PathVariable String orderNo)
    {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        TOrder order = tOrderService.getOne(wrapper);
        return R.ok().data("item",order);
    }
    //通过课程id和用户id去查询订单表，查询订单状态,其他模块要调用
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId)
    {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);
        int count = tOrderService.count(wrapper);
        if (count>0)
        {
            //已经支付
            return true;
        }
        return false;
    }
    //删除订单
    @DeleteMapping("deleteOrderbyId/{id}")
    public R deleteOrderbyId(@PathVariable String id)
    {
        tOrderService.deleteOrder(id);
        return R.ok();
    }
    //条件查询显示订单
    @PostMapping("getOrders/{current}/{limit}")
    public R getOrders(@PathVariable("current") long current,
                       @PathVariable("limit") long limit,
                       @RequestBody OrderQuery orderQuery)
    {
        Page<TOrder> tOrderPage = new Page<>(current,limit);
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        String UserName = orderQuery.getNickname();
        String CourseName = orderQuery.getCourseTitle();
        String begin = orderQuery.getBegin();
        String end = orderQuery.getEnd();
        if(!StringUtils.isEmpty(UserName))
        {
            wrapper.like("nickname",UserName);
        }
        if (!StringUtils.isEmpty(CourseName))
        {
            wrapper.like("course_title",CourseName);
        }
        if (!StringUtils.isEmpty(begin))
        {
            //数据库中字段名称
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end))
        {
            wrapper.le("gmt_modified",end);
        }
        tOrderService.page(tOrderPage,wrapper);
        long total = tOrderPage.getTotal();//总记录数
        List<TOrder> records = tOrderPage.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }



}

