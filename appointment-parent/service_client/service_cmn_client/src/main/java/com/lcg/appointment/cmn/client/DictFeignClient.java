package com.lcg.appointment.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn") //填写要调用模块的名称
@Repository //此注解不加在调用端会报错,但报错可以忽略;
public interface DictFeignClient {
    //需要调用cmn模块的哪个方法,就到cmn模块中将方法复制过来,而且绑定的url要补充完整
    //根据dictCode和value查询
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    String getName(@PathVariable String dictCode, @PathVariable String value);
    //根据value查询(value唯一的话)
    @GetMapping("/admin/cmn/dict/getName/{value}")
    String getName(@PathVariable String value);
}
