package com.lcg.appointment.hosp.controller;

import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.hosp.service.DepartmentService;
import com.lcg.appointment.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //查询医院所有科室列表
    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("getDepList/{hoscode}")
    public Result getDepList(@PathVariable String hoscode) {
        //在实体类DepartmentVo中有下级节点这一个成员
        List<DepartmentVo> list = departmentService.findDepTree(hoscode);
        return Result.ok(list);
    }

}
