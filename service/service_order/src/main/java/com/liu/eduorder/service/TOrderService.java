package com.liu.eduorder.service;

import com.liu.eduorder.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-01-14
 */
public interface TOrderService extends IService<TOrder> {

    String createOrders(String courseId, String userId);

    void deleteOrder(String id);
}
