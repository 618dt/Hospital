package com.lcg.appointment.hosp.repository;

import com.lcg.appointment.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    //按照Spring Data的规则构造函数查询科室数据
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
