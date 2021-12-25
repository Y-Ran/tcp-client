package com.yiran.client.factory;

import com.yiran.client.Connection;
import com.yiran.client.TcpClientConfig;

import java.util.function.Function;

public class NormalConnectionFactory<REQ, RES> implements ConnectionFactory<REQ, RES> {

    private final TcpClientConfig<REQ, RES> tcpClientConfig;

    private final Function<TcpClientConfig<REQ, RES>, Connection<REQ, RES>> createConnectionFunc;

    /**
     * @param tcpClientConfig      tcp连接配置
     * @param createConnectionFunc 创建tcp连接
     */
    public NormalConnectionFactory(TcpClientConfig<REQ, RES> tcpClientConfig, Function<TcpClientConfig<REQ, RES>, Connection<REQ, RES>> createConnectionFunc) {
        this.tcpClientConfig = tcpClientConfig;
        this.createConnectionFunc = createConnectionFunc;
    }

    @Override
    public Connection<REQ, RES> getConnection() {
        return createConnectionFunc.apply(tcpClientConfig);
    }

}
