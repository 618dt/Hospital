package com.lcg.appointment.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lcg.appointment.cmn.client.DictFeignClient;
import com.lcg.appointment.hosp.repository.HospitalRepository;
import com.lcg.appointment.hosp.service.HospitalService;
import com.lcg.appointment.model.hosp.Hospital;
import com.lcg.appointment.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    //上传医院接口
    @Override
    public void save(Map<String, Object> paramMap) {
        /*把参数map集合转换为对象Hospital,分两个步骤*/
        //1.将map转换成json字符串
        String mapString = JSONObject.toJSONString(paramMap);
        //2.将json字符串转换为对象
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);
        /*判断是否存在数据,如果不存在,进行添加;存在则进行修改*/
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospitalExist != null) {//存在修改
            hospitalExist.setAddress(hospital.getAddress());
            hospitalExist.setHosname(hospital.getHosname());
            hospitalExist.setIntro(hospital.getIntro());
            hospitalExist.setRoute(hospital.getRoute());
            hospitalExist.setUpdateTime(new Date());
            hospitalExist.setIsDeleted(0);
           /*修改数据传入的参数是查询得到的,MongoRepository会因此判断是否为新的数据
           * 若不是新的数据,则会执行修改操作:*/
            hospitalRepository.save(hospitalExist);
        }else {//不存在,添加
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    //根据医院编号获取医院数据
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    //条件分页查询医院列表
    @Override
    public Page<Hospital> selectHospitalPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        //创建pageable对象
        Pageable pageable = PageRequest.of(page - 1, limit,sort);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Hospital> example = Example.of(hospital,matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        //获取查询list集合,流式遍历进行医院等级封装
        pages.getContent().stream().forEach(item ->{
            this.setHospitalHosType(item);
        });
        return pages;
    }

    //修改状态
    @Override
    public void updateStatus(String id, Integer status) {
        if (status.intValue() == 0 || status.intValue() == 1) {
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    //查看医院详情,并且封装等级
    @Override
    public Map<String, Object> show(String id) {
        /*Map<String,Object> result = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        result.put("hospital", hospital);
        //单独处理更加直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;*/
        Hospital param = hospitalRepository.findById(id).get();
        return this.hospitalFind(param);
    }

    //根据医院名称查询医院列表
    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    //根据医院编号获取医院详情,并且封装等级
    @Override
    public Map<String, Object> item(String hoscode) {
        Hospital param = this.getByHoscode(hoscode);
        return this.hospitalFind(param);
    }


    //医院等级封装
    private Hospital setHospitalHosType(Hospital hospital) {
        //根据dictCode和value获取医院等级名称
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        //查询省、市、地;
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("fullAddress", provinceString + cityString + districtString);
        hospital.getParam().put("hostypeString", hostypeString);
        return hospital;
    }

    //查询医院详情
    private Map<String, Object> hospitalFind(Hospital param) {
        Map<String,Object> result = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(param);
        result.put("hospital", hospital);
        //单独处理更加直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }
}
