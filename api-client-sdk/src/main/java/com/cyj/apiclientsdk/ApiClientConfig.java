package com.cyj.apiclientsdk;

import com.cyj.apiclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @ClassName ApiClientConfig
 * @Description TODO
 * @Author chixiaowai
 * @Date 2023/8/24 19:22
 * @Version 1.0
 */

@Data
@Configuration
@Component
@ConfigurationProperties("api.client")
public class ApiClientConfig {
    private String accessKey;
    private String secretKey;

    public ApiClient apiClient(){
        return new ApiClient(accessKey, secretKey);
    }
}
