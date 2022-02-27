package com.lcg.appointment.msm.controller;

import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.msm.service.MsmService;
import com.lcg.appointment.msm.utils.RandomUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/msm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    /*使用redis来实现验证码的时效功能*/
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送手机验证码
    @ApiOperation(value = "发送手机验证码")
    @GetMapping("send/{phone}")
    public Result sendCode(@PathVariable String phone) {
        //从redis获取验证码,如果获取获取到,返回ok
        // key 手机号  value 验证码
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return Result.ok(code);
        }
        //如果从redis获取不到,
        // 生成验证码,
        code = RandomUtil.getFourBitRandom();
        //调用service方法,通过整合短信服务进行发送
        //boolean isSend = msmService.send(phone,code);
        boolean isSend = true;
        //生成验证码放到redis里面,设置有效时间为2分钟
        if(isSend) {
            redisTemplate.opsForValue().set(phone,code,2, TimeUnit.MINUTES);
            return Result.ok(code);
        } else {
            return Result.fail().message("发送短信失败");
        }
    }
}
