package com.lcg.appointment.hosp.controller;

import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.hosp.service.HospitalService;
import com.lcg.appointment.model.hosp.Hospital;
import com.lcg.appointment.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //医院列表(条件分页查询)
    @ApiOperation(value = "医院列表(条件分页查询)")
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectHospitalPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    //医院上下线
    @ApiOperation(value = "医院上下线")
    @GetMapping("updateStatus/{id}/{status}")
    public Result lockHosp(@PathVariable String id, @PathVariable Integer status) {
        //调用service方法修改状态
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    //查看医院详情
    @ApiOperation(value = "查看医院详情")
    @GetMapping("showHospDetail/{id}")
    public Result show(@PathVariable String id) {
        return Result.ok(hospitalService.show(id));
    }
}
