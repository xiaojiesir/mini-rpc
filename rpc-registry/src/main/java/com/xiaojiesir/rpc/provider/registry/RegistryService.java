package com.xiaojiesir.rpc.provider.registry;

import com.xiaojiesir.rpc.common.ServiceMeta;

import java.io.IOException;

public interface RegistryService {

    void register(ServiceMeta serviceMeta) throws Exception;

    void unRegister(ServiceMeta serviceMeta) throws Exception;

    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    void destroy() throws IOException;
}
