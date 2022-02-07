package com.liu.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.commonutils.R;
import com.liu.eduorder.entity.TOrder;
import com.liu.eduorder.service.TPayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/eduorder/t-pay-log")

public class TPayLogController {

    @Autowired
    private TPayLogService tPayLogService;

    //生成微信支付二维码接口
    //参数是订单号
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        //返回信息，包含二维码地址，还有其他需要的信息
        Map map = tPayLogService.createNatvie(orderNo);
        System.out.println("****返回二维码map集合:"+map);
        return R.ok().data(map);
    }

    //查询订单支付状态
    //参数是订单号，根据订单号查询支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo)
    {
        Map<String,String> map = tPayLogService.queryPayStatus(orderNo);
        System.out.println("*****查询订单状态map集合:"+map);
        if (map==null)
        {
            return R.error().message("支付出错");
        }
        //如果返回map里面不为空，通过map获取订单状态
        if (map.get("trade_state").equals("SUCCESS"))
        {
            //向支付表中加记录，并更新订单表订单状态
            tPayLogService.updateOrdersStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }



}

