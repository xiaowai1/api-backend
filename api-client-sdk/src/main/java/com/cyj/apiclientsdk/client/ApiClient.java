package com.cyj.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.cyj.apiclientsdk.model.User;
import com.cyj.apiclientsdk.utils.SignUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ApiClient
 * @Description 调用第三方接口的客户端
 * @Author chixiaowai
 * @Date 2023/8/24 19:27
 * @Version 1.0
 */

public class ApiClient {

    private String accessKey;
    private String secretKey;
    //网关端口
    private static final String GATEWAY_HOST = "http://localhost:8123";

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }


    /**
     * 获取用户名
     * @param user 传入的用户
     * @return
     */
    public String getUserName(User user){
        String userJson = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post(GATEWAY_HOST + "/api/name/getUsername")
                .addHeaders(getHeaderMap(userJson))
                .body(userJson)
                .execute();
        System.out.println("response.status:" + response.getStatus());
        String result = response.body();
        System.out.println("result:" + result);
        return result;
    }

    private Map<String, String> getHeaderMap(String body){
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        //secretKey不能发送给后端
//        map.put("secretKey", secretKey);
        //随机数，防止重放
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("body", body);
        //时间戳，定时删除随机数，减少空间浪费
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", SignUtil.getSign(body, secretKey));
        return map;
    }

}
