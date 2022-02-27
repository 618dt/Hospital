package com.lcg.appointment.user.client;

import com.lcg.appointment.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "service-user")
@Repository
public interface PatientFeignClient {
    //根据就诊人id获取就诊人信息
    @GetMapping("/api/user/patient/inner/get/{id}")
    Patient getPatientOrder(@PathVariable("id") Long id);

    //获取就诊人列表
    @GetMapping("/api/user/patient/inner/findAll")
    List<Patient> patientList();
}
