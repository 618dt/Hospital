package com.lcg.appointment.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcg.appointment.model.order.OrderInfo;
import com.lcg.appointment.model.order.PaymentInfo;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {

    void savePaymentInfo(OrderInfo order, Integer status);

    void paySuccess(String out_trade_no, Map<String, String> resultMap);

    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);

}
