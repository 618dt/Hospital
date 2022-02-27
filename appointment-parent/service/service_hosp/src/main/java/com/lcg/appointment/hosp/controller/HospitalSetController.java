package com.lcg.appointment.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.common.utils.MD5;
import com.lcg.appointment.hosp.service.HospitalSetService;
import com.lcg.appointment.model.hosp.HospitalSet;
import com.lcg.appointment.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院管理设置")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    //注入service服务
    @Autowired
    private HospitalSetService hospitalSetService;

    //http://localhost:8201/admin/hosp/hospitalSet/相关操作

    //1.查询医院设置表所有信息
    @ApiOperation(value = "获取所有医院设置信息")
    @GetMapping("findAll") //绑定url地址
    public Result findAllHospitalSet() {
        //调用service中的方法来查询所有信息
        List<HospitalSet> list = hospitalSetService.list();
        /*返回*/
        return Result.ok(list);
    }

    //2.逻辑删除医院设置
    @ApiOperation(value = "逻辑删除医院设置信息")
    @DeleteMapping("{id}") //删除URL映射
    public Result removeHospitalSet(@PathVariable Long id) {
       boolean flag = hospitalSetService.removeById(id);
       if(flag){//删除成功
           return Result.ok();
       }else {
           return Result.fail();//返回失败
       }
    }

    //3.条件查询带分页
    /*@RequestBody(required = false)方法表示传入的参数形式为json,且参数不是必须的,这样
    * 只有使用post方法提交才能得到RequestBody传过来的参数值*/
    @ApiOperation(value = "条件查询带分页")
    @PostMapping("findPage/{current}/{limit}")//采用路径查询方法：/当前页/每页记录数
    public Result findPageHospitalSet(@PathVariable long current,
                                      @PathVariable long limit,
                                      @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //创建page对象,传递当前页、每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();//医院名称
        String hoscode = hospitalSetQueryVo.getHoscode();//医院编号
        //当传入的参数不为空时才设置条件,否则无条件查询
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(hoscode)) {
            wrapper.eq("hoscode", hoscode);
        }
        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
        //返回结果
        return Result.ok(pageHospitalSet);
    }

    //4.添加医院设置
    @ApiOperation(value = "添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置状态 1-可以使用,0-不能使用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random = new Random();
          //将当前时间和1000以内的随机数生成MD5摘要作为签名
        String signKey = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(signKey);
        //调用service的添加方法
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //5.根据id获取医院设置
    @ApiOperation("根据id获取医院设置")
    @GetMapping("getHospitalSet/{id}")
    public Result getHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //6.修改医院设置
    @ApiOperation("修改医院设置")
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //7.批量删除医院设置
    @ApiOperation("批量删除医院设置")
    @PostMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    //8.医院设置锁定和解锁
    @ApiOperation(value = "医院设置锁定与解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                               @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法修改
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    //9.发送签名密钥
    @ApiOperation(value = "发送签名密钥")
    @PutMapping("sendKey/{id}")
    public Result sendKey(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String singKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }

}
