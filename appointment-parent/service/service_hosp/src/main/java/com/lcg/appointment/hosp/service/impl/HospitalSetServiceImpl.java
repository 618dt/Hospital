package com.lcg.appointment.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcg.appointment.common.exception.HospitalException;
import com.lcg.appointment.common.result.ResultCodeEnum;
import com.lcg.appointment.hosp.mapper.HospitalSetMapper;
import com.lcg.appointment.hosp.service.HospitalSetService;
import com.lcg.appointment.model.hosp.Hospital;
import com.lcg.appointment.model.hosp.HospitalSet;
import com.lcg.appointment.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    //根据传递过来的医院编码,查询数据库,查询签名
    @Override
    public String getSignKey(String hoscode) {
        return this.getHospitalSet(hoscode).getSignKey();
    }

    //根据医院编码查询签名信息
    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(this.getHospitalSet(hoscode).getApiUrl());
        signInfoVo.setSignKey(this.getHospitalSet(hoscode).getSignKey());
        return signInfoVo;
    }

    //根据医院编码查询医院
    public HospitalSet getHospitalSet(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new HospitalException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        return hospitalSet;
    }

}
