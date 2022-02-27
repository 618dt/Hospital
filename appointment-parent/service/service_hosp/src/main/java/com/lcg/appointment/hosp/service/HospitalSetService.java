package com.lcg.appointment.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcg.appointment.model.hosp.HospitalSet;
import com.lcg.appointment.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    //根据传递过来的医院编码,查询数据库,查询签名
    String getSignKey(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);

}
