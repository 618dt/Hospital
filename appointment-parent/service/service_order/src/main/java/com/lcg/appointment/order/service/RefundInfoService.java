package com.lcg.appointment.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcg.appointment.model.order.PaymentInfo;
import com.lcg.appointment.model.order.RefundInfo;

public interface RefundInfoService extends IService<RefundInfo> {
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}
