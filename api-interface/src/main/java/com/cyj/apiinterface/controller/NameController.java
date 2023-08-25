package com.cyj.apiinterface.controller;

import com.cyj.apiclientsdk.model.User;
import com.cyj.apiclientsdk.utils.SignUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName NameController
 * @Description 模拟接口服务
 * @Author chixiaowai
 * @Date 2023/8/24 20:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @PostMapping("/getUsername")
    public String getUsernameByUser(@RequestBody User user, HttpServletRequest request){
        // TODO 此处校验应写到网关中
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String body = request.getHeader("body");
        String sign = request.getHeader("sign");

        // TODO 此处的"cyj"应从数据库中查询
        if (!"cyj".equals(accessKey)) {
            throw new RuntimeException("无权限");
        }
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        long currentTime = System.currentTimeMillis() / 1000;
        Long FIVE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) > FIVE_MINUTES) {
            throw new RuntimeException("无权限");
        }
        // TODO secretKey是从数据库中查询出来的
        String serveSign = SignUtil.getSign(body, "nidemei");
        if (!serveSign.equals(sign)) {
            throw new RuntimeException("无权限");
        }
        return "用户名是" + user.getUsername();
    }
}
