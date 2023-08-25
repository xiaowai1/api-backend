package com.cyj.apiinterface;

import com.cyj.apiclientsdk.client.ApiClient;
import com.cyj.apiclientsdk.model.User;

/**
 * @ClassName Main
 * @Description 用户调用接口模拟
 * @Author chixiaowai
 * @Date 2023/8/24 20:15
 * @Version 1.0
 */

public class Main {
    public static void main(String[] args) {
        String accessKey = "cyj";
        String secretKey = "nidemei";
        ApiClient apiClient = new ApiClient(accessKey, secretKey);
        User user = new User();
        user.setUsername("cyj_username");
        String usernameByPost = apiClient.getUserName(user);
        System.out.println("***********************************************");
        System.out.println("usernameByPost:" + usernameByPost);
    }
}
