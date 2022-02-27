package com.lcg.appointment.statistics.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.model.order.OrderInfo;
import com.lcg.appointment.model.user.Patient;
import com.lcg.appointment.order.client.OrderFeignClient;
import com.lcg.appointment.user.client.PatientFeignClient;
import com.lcg.appointment.vo.order.OrderCountQueryVo;
import com.lcg.appointment.vo.order.OrderQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/statistics")
public class StatisticsController {

    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private PatientFeignClient patientFeignClient;

    //获取预约统计数据
    @ApiOperation(value = "获取预约统计数据")
    @GetMapping("getCountMap")
    public Result getCountMap(OrderCountQueryVo orderCountQueryVo) {
        Map<String, Object> countMap = orderFeignClient.getCountMap(orderCountQueryVo);
        return Result.ok(countMap);
    }

    //获取就诊人列表
    @ApiOperation(value = "获取就诊人列表")
    @GetMapping("getPatients")
    public Result patientList() {
        List<Patient> list = patientFeignClient.patientList();
        return Result.ok(list);
    }

    //获取订单列表
    @ApiOperation(value = "平台订单列表")
    @GetMapping("getOrders/{page}/{limit}")
    public Result OrderList(@PathVariable Long page, @PathVariable Long limit, OrderQueryVo orderQueryVo) {
        Page<OrderInfo> pageModel = orderFeignClient.OrderList(page, limit, orderQueryVo);
        return Result.ok(pageModel);
    }

    //根据订单id查询订单详情
    @ApiOperation(value = "平台查询订单详情")
    @GetMapping("getOrderInfo/{orderId}")
    public Result getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId);
        return Result.ok(orderInfo);
    }
}
