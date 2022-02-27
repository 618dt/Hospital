package com.lcg.appointment.hosp.controller.api;

import com.lcg.appointment.common.exception.HospitalException;
import com.lcg.appointment.common.helper.HttpRequestHelper;
import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.common.result.ResultCodeEnum;
import com.lcg.appointment.hosp.service.DepartmentService;
import com.lcg.appointment.hosp.service.HospitalService;
import com.lcg.appointment.hosp.service.HospitalSetService;
import com.lcg.appointment.hosp.service.ScheduleService;
import com.lcg.appointment.model.hosp.Department;
import com.lcg.appointment.model.hosp.Hospital;
import com.lcg.appointment.model.hosp.Schedule;
import com.lcg.appointment.vo.hosp.DepartmentQueryVo;
import com.lcg.appointment.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    //上传医院接口
    @ApiOperation(value = "上传医院接口")
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        //获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        /*调用工具类中的方法将Map中的值的类型从String[]转换为Object;
        * 在这里不进行转换的话后期处理比较麻烦*/
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        /*进行签名校验*/
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        if(!HttpRequestHelper.isSignEquals(paramMap,signKey)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        //传输过程中"+"转换为了" ",因此我们要转换回来
        String logoData = (String)paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);
        //调用service的方法将数据保存到mongo数据库
        hospitalService.save(paramMap);
        return Result.ok();
    }

    //根据编号查询医院
    @ApiOperation(value = "根据编号查询医院")
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        //获取传递过来医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        if(!HttpRequestHelper.isSignEquals(paramMap,signKey)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        //调用service方法实现根据医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    //上传科室接口
    @ApiOperation(value = "上传科室接口")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        if(!HttpRequestHelper.isSignEquals(paramMap,signKey)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        //调用方法上传科室数据
        departmentService.save(paramMap);
        return Result.ok();
    }

    //查询科室接口
    @ApiOperation(value = "查询科室接口")
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request) {
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取相关信息
        String hoscode = (String) paramMap.get("hoscode");
        String page =  (String) paramMap.get("page");
        //当前页,如果页数为空,当前页为1;
        int curPage = StringUtils.isEmpty(page) ? 1 : Integer.parseInt(page);
        //每页记录数,默认为5
        String lim = (String) paramMap.get("limit");
        int limit = StringUtils.isEmpty(lim)?5:Integer.parseInt(lim);
        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        if(!HttpRequestHelper.isSignEquals(paramMap,signKey)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        //封装的查询条件类
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //调用service方法
        Page<Department> pageModel = departmentService.findPageDepartment(curPage, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    //删除科室接口
    @ApiOperation(value = "删除科室接口")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        if(!HttpRequestHelper.isSignEquals(paramMap,signKey)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    //上传排班接口
    @ApiOperation(value = "上传排班接口")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        if(!HttpRequestHelper.isSignEquals(paramMap,signKey)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.save(paramMap);
        return Result.ok();
    }

    //查询排班接口
    @ApiOperation(value = "查询排班接口")
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String page =  (String) paramMap.get("page");
        //当前页,如果页数为空,当前页为1;
        int curPage = StringUtils.isEmpty(page) ? 1 : Integer.parseInt(page);
        //每页记录数,默认为5
        String lim = (String) paramMap.get("limit");
        int limit = StringUtils.isEmpty(lim)?5:Integer.parseInt(lim);
        if(!HttpRequestHelper.isSignEquals(paramMap,signKey)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        //调用service方法查询
        Page<Schedule> pageModel = scheduleService.findPageSchedule(curPage, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    //删除排班
    @ApiOperation(value = "删除排班")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        if(!HttpRequestHelper.isSignEquals(paramMap,signKey)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }
}
