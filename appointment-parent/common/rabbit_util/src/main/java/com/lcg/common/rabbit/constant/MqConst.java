package com.lcg.common.rabbit.constant;

public class MqConst {
    //预约下单
    public static final String EXCHANGE_DIRECT_ORDER = "exchange.direct.order";
    public static final String ROUTING_ORDER = "order";
    //下单队列
    public static final String QUEUE_ORDER = "queue.order";
    //短信
    public static final String EXCHANGE_DIRECT_MSM = "exchange.direct.msm";
    public static final String ROUTING_MSM_ITEM = "msm.item";
    //短信队列
    public static final String QUEUE_MSM_ITEM = "queue.msm.item";
    //定时任务
    public static final String QUEUE_TASK_8 = "queue.task.8";
    public static final String EXCHANGE_DIRECT_TASK = "exchange.direct.task";
    public static final String ROUTING_TASK_8 = "task.8";
}
