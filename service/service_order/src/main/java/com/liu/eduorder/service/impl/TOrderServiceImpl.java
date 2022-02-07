package com.liu.eduorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.eduorder.client.courseClient;
import com.liu.eduorder.client.userClient;
import com.liu.eduorder.entity.EduCourse;
import com.liu.eduorder.entity.TOrder;
import com.liu.eduorder.entity.TPayLog;
import com.liu.eduorder.entity.UcenterMember;
import com.liu.eduorder.mapper.TOrderMapper;
import com.liu.eduorder.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.eduorder.service.TPayLogService;
import com.liu.eduorder.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-01-14
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private userClient user;

    @Autowired
    private courseClient course;

    @Autowired
    private TPayLogService tPayLogService;

    //删除订单
    @Override
    public void deleteOrder(String id) {
        TOrder tOrder = baseMapper.selectById(id);
        String orderNo = tOrder.getOrderNo();
        QueryWrapper<TPayLog> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        TPayLog one = tPayLogService.getOne(wrapper);
        if(one!=null)
        {

            tPayLogService.removeById(one);
        }
        baseMapper.deleteById(tOrder);

    }

    //创建订单
    @Override
    public String createOrders(String courseId, String userId) {
        //通过远程调用获取用户信息
        UcenterMember member = user.getUserInfoById(userId);
        //通过远程调用获取课程信息
        EduCourse courses = course.getCoureseInfoOrder(courseId);

        TOrder order = new TOrder();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courses.getId());
        order.setCourseTitle(courses.getTitle());
        order.setCourseCover(courses.getCover());
        String teacherName = course.getTeacherNameById(courses.getTeacherId());
        order.setTeacherName(teacherName);
        order.setTotalFee(courses.getPrice());
        order.setMemberId(userId);
        order.setMobile(member.getMobile());
        order.setNickname(member.getNickname());
        order.setStatus(0);//支付状态
        order.setPayType(1);//支付类型
        baseMapper.insert(order);
        return order.getOrderNo();
    }
}
