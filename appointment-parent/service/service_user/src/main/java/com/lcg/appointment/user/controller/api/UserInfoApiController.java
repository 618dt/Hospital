package com.lcg.appointment.user.controller.api;

import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.common.utils.AuthContextHolder;
import com.lcg.appointment.model.user.UserInfo;
import com.lcg.appointment.user.service.UserInfoService;
import com.lcg.appointment.vo.user.LoginVo;
import com.lcg.appointment.vo.user.UserAuthVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {
    @Autowired
    private UserInfoService userInfoService;

    //用户手机号登陆接口
    @ApiOperation(value = "用户手机号登陆接口")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }

    //用户认证接口
    @ApiOperation(value = "用户认证接口")
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        //传递用户ID和认证数据的vo对象两个参数;
        Long userId = AuthContextHolder.getUserId(request);
        userInfoService.userAuth(userId, userAuthVo);
        return Result.ok();
    }

    //获取用户ID信息接口
    @ApiOperation(value = "获取用户ID信息接口")
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}
