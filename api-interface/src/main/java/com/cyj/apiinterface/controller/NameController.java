package com.cyj.apiinterface.controller;

import com.cyj.apiclientsdk.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String getUsernameByUser(@RequestBody User user){
        return "用户名是" + user.getUsername();
    }
}
