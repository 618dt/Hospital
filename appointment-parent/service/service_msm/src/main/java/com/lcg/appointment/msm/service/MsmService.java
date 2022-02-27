package com.lcg.appointment.msm.service;

import com.lcg.appointment.vo.msm.MsmVo;

public interface MsmService {

    boolean send(String phone, String code);

    boolean send(MsmVo msmVo);

}
