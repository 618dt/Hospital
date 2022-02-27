package com.lcg.appointment.msm.service.impl;

import com.lcg.appointment.msm.service.MsmService;
import com.lcg.appointment.msm.utils.SendmailUtil;
import com.lcg.appointment.vo.msm.MsmVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    //发送验证码
    @Override
    public boolean send(String phone, String code) {
        //判断手机号是否为空
        if(StringUtils.isEmpty(phone)) {
            return false;
        }
        //调用方法发送邮件,并返回
        return SendmailUtil.sendCode(phone, code);
    }

    //rabbitmq发送消息通知
    @Override
    public boolean send(MsmVo msmVo) {
        if (StringUtils.isEmpty(msmVo.getPhone())) {
            return false;
        }
        return this.sendMail(msmVo.getPhone(), msmVo.getMessage());
    }
    private boolean sendMail(String phone,String message) {
        if (!StringUtils.isEmpty(phone)) {
            StringBuilder builder = new StringBuilder();
            builder.append(message);
            String content = builder.toString();
            boolean isSend = SendmailUtil.send(phone,content);
            return isSend;
        }
        return false;
    }
}
