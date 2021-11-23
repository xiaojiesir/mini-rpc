package com.xiaojiesir.rpc.handler;

import com.xiaojiesir.rpc.common.MiniRpcFuture;
import com.xiaojiesir.rpc.common.MiniRpcRequestHolder;
import com.xiaojiesir.rpc.common.MiniRpcResponse;
import com.xiaojiesir.rpc.protocol.MiniRpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcResponseHandler extends SimpleChannelInboundHandler<MiniRpcProtocol<MiniRpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MiniRpcProtocol<MiniRpcResponse> msg) {
        long requestId = msg.getHeader().getRequestId();
        MiniRpcFuture<MiniRpcResponse> future = MiniRpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(msg.getBody());
    }
}
