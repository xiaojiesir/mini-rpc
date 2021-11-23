package com.xiaojiesir.rpc.provider;

import com.xiaojiesir.rpc.common.RpcProperties;
import com.xiaojiesir.rpc.provider.registry.RegistryFactory;
import com.xiaojiesir.rpc.provider.registry.RegistryService;
import com.xiaojiesir.rpc.provider.registry.RegistryType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcProviderAutoConfiguration {

    @Resource
    private RpcProperties rpcProperties;

    @Bean
    public RpcProvider init() throws Exception {
        RegistryType type = RegistryType.valueOf(rpcProperties.getRegistryType());
        //构建单例注册服务
        RegistryService serviceRegistry = RegistryFactory.getInstance(rpcProperties.getRegistryAddr(), type);
        return new RpcProvider(rpcProperties.getServicePort(), serviceRegistry);
    }
}
