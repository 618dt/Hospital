package com.lcg.appointment.hosp.repository;

import com.lcg.appointment.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    //判断是否存在数据
    Hospital getHospitalByHoscode(String hoscode);

    //根据名称模糊查询医院列表
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
