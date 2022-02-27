package com.lcg.appointment.common.exception;

import com.lcg.appointment.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    //如果程序执行了Exception类型的异常,则会调用下面定义的方法;
    @ExceptionHandler(Exception.class)
    @ResponseBody //可以将返回结果使用JSON输出到前端
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(HospitalException.class)
    @ResponseBody
    public Result error(HospitalException e) {
        e.printStackTrace();
        return Result.fail();
    }
}
