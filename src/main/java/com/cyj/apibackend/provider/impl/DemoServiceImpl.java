package com.cyj.apibackend.provider.impl;

import com.cyj.apibackend.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @ClassName DemoServiceImpl
 * @Description TODO
 * @Author chixiaowai
 * @Date 2023/8/27 13:16
 * @Version 1.0
 */
@DubboService
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        System.out.println("Hello " + name + ", request from consumer: " + RpcContext.getServiceContext().getRemoteAddress());
        return "Hello " + name;
    }
}
