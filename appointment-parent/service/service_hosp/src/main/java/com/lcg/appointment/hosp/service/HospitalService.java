package com.lcg.appointment.hosp.service;

import com.lcg.appointment.model.hosp.Hospital;
import com.lcg.appointment.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    //上传医院接口
    void save(Map<String, Object> paramMap);

    //根据医院编号查询医院设置列表
    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectHospitalPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String,Object> show(String id);

    List<Hospital> findByHosname(String hosname);

    Map<String, Object> item(String hoscode);

}
