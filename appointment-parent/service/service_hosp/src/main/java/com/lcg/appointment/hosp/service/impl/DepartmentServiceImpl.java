package com.lcg.appointment.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lcg.appointment.hosp.repository.DepartmentRepository;
import com.lcg.appointment.hosp.service.DepartmentService;
import com.lcg.appointment.model.hosp.Department;
import com.lcg.appointment.vo.hosp.DepartmentQueryVo;
import com.lcg.appointment.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    //上传科室接口
    @Override
    public void save(Map<String, Object> paramMap) {
        //将参数转换为department对象
        String paramString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramString,Department.class);
        //根据医院编号和科室编号查询科室数据,判断科室是否存在
        Department departmentExist = departmentRepository.
                getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        //判断
        if (departmentExist != null) {//不为空修改
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        }else{//为空上传数据
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    //查询科室接口
    @Override
    public Page<Department> findPageDepartment(int curPage, int limit, DepartmentQueryVo departmentQueryVo) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        //创建Pageable对象,设置当前页和每页记录数,0是第一页
        Pageable pageable = PageRequest.of(curPage - 1, limit);
        //创建Example对象（MongoDB）
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department, matcher);
        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    //删除科室接口
    @Override
    public void remove(String hoscode, String depcode) {
        //根据医院编号和科室编号查询
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            //调用删除方法
            departmentRepository.deleteById(department.getId());
        }
    }

    //根据医院编号,查询医院所有科室列表
    @Override
    public List<DepartmentVo> findDepTree(String hoscode) {
        //创建list集合,用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();
        //根据医院编号,查询医院所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        //封装mongo数据库查询条件
        Example example = Example.of(departmentQuery);
        List<Department> allList = departmentRepository.findAll(example);
        //根据大科室编号(bigcode)分组,获取每个大科室的下级子科室
        //使用stream流式遍历分组(java8新特性),返回的Map集合中key为bigcode,value即子科室列表
        Map<String, List<Department>> departmentMap = allList
                .stream()
                .collect(Collectors.groupingBy(Department::getBigcode));
        //遍历Map集合
        for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            String bigcode = entry.getKey();//大科室编号
            List<Department> departments = entry.getValue();//大科室编号对应的小科室数据
            //封装大科室(根据json数据结构)
            DepartmentVo departmentVo1 = new DepartmentVo();//Vo1用于封装大科室
            departmentVo1.setDepcode(bigcode);
            departmentVo1.setDepname(departments.get(0).getBigname());
            //封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : departments) {
                DepartmentVo departmentVo2 = new DepartmentVo();//Vo2用于封装子科室
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
                children.add(departmentVo2);//封装到子科室集合中
            }
            //把小科室list集合放到大科室children里面
            departmentVo1.setChildren(children);
            //发到最终的result集合中去
            result.add(departmentVo1);
        }
        return result;
    }

    //获取科室名称
/*    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }
        return "null";
    }*/

    //根据医院编号,科室编号获取科室
    @Override
    public Department getDepartment(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        return department;
    }
}
