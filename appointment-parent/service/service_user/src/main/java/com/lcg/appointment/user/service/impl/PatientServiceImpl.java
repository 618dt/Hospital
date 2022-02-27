package com.lcg.appointment.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcg.appointment.cmn.client.DictFeignClient;
import com.lcg.appointment.enums.DictEnum;
import com.lcg.appointment.model.user.Patient;
import com.lcg.appointment.user.mapper.PatientMapper;
import com.lcg.appointment.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;
    //根据用户id获取就诊人列表
    @Override
    public List<Patient> findAllUserId(Long userId) {
        //根据userId查询出所有就诊人列表
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Patient> patientList = baseMapper.selectList(wrapper);
        //通过流式处理并远程调用相关接口,得到编码对应的具体内容,查询数据字典内容
        patientList.stream().forEach(item ->{
            this.packPatient(item);
        });
        return patientList;
    }

    //根据id获取就诊人信息
    @Override
    public Patient getPatientId(Long id) {
        return this.packPatient(baseMapper.selectById(id));
    }

    //获取所有就诊人列表,用于管理平台订单查询条件,无需封装其他信息
    @Override
    public List<Patient> findAllPatient() {
        List<Patient> patientList = baseMapper.selectList(null);
        return patientList;
    }

    //Patient对象里面其他参数封装
    private Patient packPatient(Patient patient) {
        //根据证件类型编码,获取证件类型具体值
        String certificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        //联系人证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());

        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;
    }
}
