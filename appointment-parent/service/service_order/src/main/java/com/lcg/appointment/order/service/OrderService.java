package com.lcg.appointment.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcg.appointment.model.order.OrderInfo;
import com.lcg.appointment.vo.order.OrderCountQueryVo;
import com.lcg.appointment.vo.order.OrderQueryVo;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {

    Long saveOrder(String scheduleId, Long patientId);

    OrderInfo getOrder(String orderId);

    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    Boolean cancelOrder(Long orderId);

    void patientTips();

    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);

}
