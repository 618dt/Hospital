package com.lcg.appointment.user.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.lcg.appointment.common.helper.JwtHelper;
import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.model.user.UserInfo;
import com.lcg.appointment.user.service.UserInfoService;
import com.lcg.appointment.user.utils.ConstantWxPropertiesUtils;
import com.lcg.appointment.user.utils.HttpClientUtils;
import com.sun.deploy.net.URLEncoder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

//微信操作接口
/*前面使用的是RestController,因为需要返回数据;而这里为了方便跳转,使用Controller;
* 需要返回数据的方法再添加@ResponseBody注解即可*/
@Controller
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;
    //1.生成微信扫描二维码
    //返回生成二维码需要的参数
    @ApiOperation(value = "生成微信扫描二维码")
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect() {
        Map<String, Object> map = new HashMap<>();
        //官方文档中所需的参数
        map.put("appid", ConstantWxPropertiesUtils.WX_OPEN_APP_ID);
        map.put("scope","snsapi_login");
        String wxOpenRedirectUrl = ConstantWxPropertiesUtils.WX_OPEN_REDIRECT_URL;
        //url参数需要进行URLEncoder处理
        //wxOpenRedirectUrl = URLEncoder.encode(wxOpenRedirectUrl, "utf-8");
        map.put("redirect_uri",wxOpenRedirectUrl);
        map.put("state",System.currentTimeMillis()+"");
        return Result.ok(map);
    }

    //2.回调的方法,得到扫描用户的信息
    @ApiOperation(value = "得到扫描用户的信息")
    @GetMapping("callback")
    public String callback(String code,String state) {
        //第一步 获取临时票据 code
        System.out.println("code:"+code);
        System.out.println("state:"+state);
        //第二步 拿着code和微信id和秘钥,请求微信固定地址,得到两个值
        //使用code和appid以及appscrect换取access_token
        //  %s   占位符 这里使用占位符绑定参数值,当然也可以直接将值拼接进去,这样显然比较繁琐;
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");
        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantWxPropertiesUtils.WX_OPEN_APP_ID,
                ConstantWxPropertiesUtils.WX_OPEN_APP_SECRET,
                code);
        //使用httpclient请求这个地址
        try {
            String accesstokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accesstokenInfo:"+accesstokenInfo);
            //从返回字符串获取两个值 openid  和  access_token
            JSONObject jsonObject = JSONObject.parseObject(accesstokenInfo);
            String access_token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");

            //判断数据库是否存在微信的扫描用户信息,存在信息不需要再次存入
            //根据openid判断,因为该值是唯一的
            UserInfo userInfo = userInfoService.selectWxInfoOpenId(openid);
            if(userInfo == null) { //数据库不存在用户的微信登录信息才能够将扫描的信息存入数据库
                //第三步 拿着openid  和  access_token请求微信地址,得到扫描用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String resultInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultInfo:"+resultInfo);//扫描用户信息
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
                //解析用户信息
                //用户昵称
                String nickname = resultUserInfoJson.getString("nickname");
                //用户头像
                //String headimgurl = resultUserInfoJson.getString("headimgurl");

                //获取扫描用户信息添加数据库
                userInfo = new UserInfo();
                userInfo.setNickName(nickname);
                userInfo.setOpenid(openid);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }
            //返回name和token字符串
            Map<String,String> map = new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);

            //判断userInfo是否有手机号(邮箱号),如果手机号为空,返回openid,以便将openid与手机号进行绑定;
            //如果手机号不为空,返回openid值是空字符串
            //前端判断：如果openid不为空,绑定手机号,如果openid为空,不需要绑定手机号
            if(StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "null");
            }
            //使用jwt生成token字符串
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            //跳转到前端页面,拼接上token、openid和name;
            return "redirect:" + ConstantWxPropertiesUtils.APPOINTMENT_BASE_URL + "/weixin/callback?token="+map.get("token")+ "&openid="+map.get("openid")+"&name="+URLEncoder.encode(map.get("name"),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
