package com.xiaojiesir.rpc.consumer;

import com.xiaojiesir.rpc.common.MiniRpcFuture;
import com.xiaojiesir.rpc.common.MiniRpcRequest;
import com.xiaojiesir.rpc.common.MiniRpcRequestHolder;
import com.xiaojiesir.rpc.common.MiniRpcResponse;
import com.xiaojiesir.rpc.protocol.MiniRpcProtocol;
import com.xiaojiesir.rpc.protocol.MsgHeader;
import com.xiaojiesir.rpc.protocol.MsgType;
import com.xiaojiesir.rpc.protocol.ProtocolConstants;
import com.xiaojiesir.rpc.provider.registry.RegistryService;
import com.xiaojiesir.rpc.serialization.SerializationTypeEnum;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class RpcInvokerProxy implements InvocationHandler {

    private final String serviceVersion;
    private final long timeout;
    private final RegistryService registryService;

    public RpcInvokerProxy(String serviceVersion, long timeout, RegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构造 RPC 协议对象
        MiniRpcProtocol<MiniRpcRequest> protocol = new MiniRpcProtocol<>();
        MsgHeader header = new MsgHeader();
        long requestId = MiniRpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(requestId);
        header.setSerialization((byte) SerializationTypeEnum.HESSIAN.getType());
        header.setMsgType((byte) MsgType.REQUEST.getType());
        header.setStatus((byte) 0x1);
        protocol.setHeader(header);

        MiniRpcRequest request = new MiniRpcRequest();
        request.setServiceVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        protocol.setBody(request);

        RpcConsumer rpcConsumer = new RpcConsumer();
        MiniRpcFuture<MiniRpcResponse> future = new MiniRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        MiniRpcRequestHolder.REQUEST_MAP.put(requestId, future);
        // 发起 RPC 远程调用
        rpcConsumer.sendRequest(protocol, this.registryService);

        // TODO hold request by ThreadLocal

        // 等待 RPC 调用执行结果
        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
    }
}
