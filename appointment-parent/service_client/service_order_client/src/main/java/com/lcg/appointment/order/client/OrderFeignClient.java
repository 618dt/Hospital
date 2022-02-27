package com.lcg.appointment.order.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcg.appointment.model.order.OrderInfo;
import com.lcg.appointment.vo.order.OrderCountQueryVo;
import com.lcg.appointment.vo.order.OrderQueryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "service-order")
@Repository
public interface OrderFeignClient {
    //根据排班id获取预约下单数据
    @PostMapping("/api/order/orderInfo/inner/getCountMap")
    Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo);

    //获取订单列表
    @PostMapping("/api/order/orderInfo/inner/{page}/{limit}")
    Page<OrderInfo> OrderList(@PathVariable Long page, @PathVariable Long limit, @RequestBody OrderQueryVo orderQueryVo);

    //查询订单详情
    @GetMapping("/api/order/orderInfo/inner/getOrderInfo/{id}")
    OrderInfo getOrderInfo(@PathVariable String id);
}
