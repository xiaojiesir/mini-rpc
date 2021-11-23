package com.xiaojiesir.rpc.provider.registry;

public class RegistryFactory {

    private static volatile RegistryService registryService;

    /**
     * 创建单例注册服务
     * @param registryAddr
     * @param type
     * @return
     * @throws Exception
     */
    public static RegistryService getInstance(String registryAddr, RegistryType type) throws Exception {

        if (null == registryService) {
            synchronized (RegistryFactory.class) {
                if (null == registryService) {
                    switch (type) {
                        case ZOOKEEPER:
                            registryService = new ZookeeperRegistryService(registryAddr);
                            break;
                        case EUREKA:
                            registryService = new EurekaRegistryService(registryAddr);
                            break;
                    }
                }
            }
        }
        return registryService;
    }
}
