package com.lcg.appointment.user.controller.api;

import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.common.utils.AuthContextHolder;
import com.lcg.appointment.model.user.Patient;
import com.lcg.appointment.user.service.PatientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//就诊人管理接口
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    //获取就诊人列表
    @ApiOperation(value = "获取当前用户就诊人列表")
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllUserId(userId);
        return Result.ok(list);
    }

    //添加就诊人
    @ApiOperation(value = "添加就诊人")
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    //根据就诊人id获取就诊人信息
    @ApiOperation(value = "根据就诊人id获取就诊人信息")
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }

    //修改就诊人
    @ApiOperation(value = "修改就诊人")
    @PostMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }

    //删除就诊人
    @ApiOperation(value = "删除就诊人")
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.ok();
    }

    //根据就诊人id获取就诊人信息
    @ApiOperation(value = "内部获取就诊人信息")
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return patient;
    }

    //内部获取所有就诊人列表
    @ApiOperation(value = "获取所有就诊人列表")
    @GetMapping("inner/findAll")
    public List<Patient> patientList() {
        System.out.println("正在调用获取所有就诊人列表");
        List<Patient> list = patientService.findAllPatient();
        return list;
    }
}
