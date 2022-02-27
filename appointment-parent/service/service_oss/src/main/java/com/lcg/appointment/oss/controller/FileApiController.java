package com.lcg.appointment.oss.controller;

import com.lcg.appointment.common.result.Result;
import com.lcg.appointment.oss.service.FileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Autowired
    private FileService fileService;
    //上传文件到阿里云oss
    @ApiOperation(value = "上传文件到阿里云oss")
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) {
        //从前端浏览器获取上传文件,然后调用service方法上传至阿里云OSS
        String url = fileService.upload(file);
        //上传之后,在bucket中会有文件的url,则可以通过此url将用户上传的图片进行前端展示;
        System.out.println("图片url为" + url);
        return Result.ok(url);
    }
}
