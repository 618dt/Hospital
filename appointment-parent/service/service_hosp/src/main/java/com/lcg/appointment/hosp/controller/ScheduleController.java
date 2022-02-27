package com.lcg.appointment.hosp.controller;

import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.hosp.service.ScheduleService;
import com.lcg.appointment.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    //根据 医院编号 和 科室编号 查询按日期分组的排班规则数据
    @ApiOperation(value = "查询按日期分组的排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable long page, @PathVariable long limit, @PathVariable String hoscode, @PathVariable String depcode){
        Map<String,Object> map = scheduleService.getScheduleRule(page, limit, hoscode, depcode);
        return Result.ok(map);
    }

    //根据 医院编号 和 科室编号 和工作日期 查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode, @PathVariable String depcode, @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode, depcode, workDate);
        return Result.ok(list);
    }

}
