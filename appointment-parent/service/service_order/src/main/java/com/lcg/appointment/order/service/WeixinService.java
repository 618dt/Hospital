package com.lcg.appointment.order.service;

import java.util.Map;

public interface WeixinService {
    Map createNative(Long orderId);

    Map<String, String> queryPayStatus(Long orderId);

    Boolean refund(Long orderId);

}
