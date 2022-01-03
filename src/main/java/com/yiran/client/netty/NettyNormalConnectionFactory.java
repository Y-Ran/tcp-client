package com.yiran.client.netty;

import com.yiran.client.Connection;
import com.yiran.client.TcpClientConfig;
import com.yiran.client.factory.NormalConnectionFactory;

public class NettyNormalConnectionFactory<REQ, RES> extends NormalConnectionFactory<REQ, RES> {

    /**
     * @param tcpClientConfig tcp连接配置
     */
    public NettyNormalConnectionFactory(TcpClientConfig<REQ, RES> tcpClientConfig) {
        super(tcpClientConfig);
    }

    @Override
    protected Connection<REQ, RES> createConnection(TcpClientConfig<REQ, RES> tcpClientConfig) {
        // 使用匿名子类 保证netty可以正确解析泛型参数
        return new NettyConnectionImpl<>(tcpClientConfig) {
        };
    }

}
