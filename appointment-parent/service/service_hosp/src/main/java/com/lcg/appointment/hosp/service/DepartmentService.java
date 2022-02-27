package com.lcg.appointment.hosp.service;


import com.lcg.appointment.model.hosp.Department;
import com.lcg.appointment.vo.hosp.DepartmentQueryVo;
import com.lcg.appointment.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(int curPage, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDepTree(String hoscode);

    //String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);

}
