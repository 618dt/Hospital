package com.lcg.appointment.order.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.common.utils.AuthContextHolder;
import com.lcg.appointment.enums.OrderStatusEnum;
import com.lcg.appointment.model.order.OrderInfo;
import com.lcg.appointment.order.service.OrderService;
import com.lcg.appointment.vo.order.OrderCountQueryVo;
import com.lcg.appointment.vo.order.OrderQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    //生成挂号订单
    @ApiOperation(value = "生成挂号订单")
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result savaOrders(@PathVariable String scheduleId,
                             @PathVariable Long patientId) {
        Long orderId = orderService.saveOrder(scheduleId,patientId);
        return Result.ok(orderId);
    }

    //根据订单id查询订单详情
    @ApiOperation(value = "根据订单id查询订单详情")
    @GetMapping("auth/getOrderInfo/{orderId}")
    public Result getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }

    //订单列表（条件查询带分页）
    @ApiOperation(value = "订单列表(条件分页)")
    @GetMapping("auth/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       OrderQueryVo orderQueryVo, HttpServletRequest request) {
        //通过工具类设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        IPage<OrderInfo> pageModel =
                orderService.selectPage(pageParam,orderQueryVo);
        return Result.ok(pageModel);
    }

    //获取订单状态列表
    @ApiOperation(value = "获取订单状态列表")
    @GetMapping("auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    //取消预约
    @ApiOperation(value = "取消预约")
    @GetMapping("auth/cancelOrder/{orderId}")
    public Result cancelOrder(@PathVariable Long orderId) {
        Boolean isOrder = orderService.cancelOrder(orderId);
        return Result.ok(isOrder);
    }

    /*网关已经设置inner的路径不能够直接访问所以需要在内部访问
     *在statistics模块中调用平台订单列表,订单详情和订单统计数据的接口
     */
    //获取订单统计数据
    @ApiOperation(value = "获取订单统计数据")
    @PostMapping("inner/getCountMap")
    public Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        return orderService.getCountMap(orderCountQueryVo);
    }

    //平台获取订单列表,由于查询所有的订单列表,所有不需要获取用户id
    @ApiOperation(value = "平台订单列表")
    @PostMapping("inner/{page}/{limit}")
    public Page<OrderInfo> OrderList(@PathVariable Long page, @PathVariable Long limit, @RequestBody OrderQueryVo orderQueryVo) {
        //设置分页参数:页号,每页记录数
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        IPage<OrderInfo> pageModel = orderService.selectPage(pageParam, orderQueryVo);
        /*远程服务调用需要返回Page类型*/
        BeanUtils.copyProperties(pageModel,pageParam);
        return pageParam;
    }
    //平台查询订单详情
    @ApiOperation(value = "平台查询订单详情")
    @GetMapping("inner/getOrderInfo/{id}")
    public OrderInfo getOrderInfo(@PathVariable String id) {
        OrderInfo orderInfo = orderService.getOrder(id);
        return orderInfo;
    }

}
