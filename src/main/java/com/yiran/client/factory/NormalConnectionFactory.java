package com.yiran.client.factory;

import com.yiran.client.Connection;
import com.yiran.client.TcpClientConfig;

public abstract class NormalConnectionFactory<REQ, RES> implements ConnectionFactory<REQ, RES> {

    private final TcpClientConfig<REQ, RES> tcpClientConfig;

    /**
     * @param tcpClientConfig tcp连接配置
     */
    public NormalConnectionFactory(TcpClientConfig<REQ, RES> tcpClientConfig) {
        this.tcpClientConfig = tcpClientConfig;
    }

    @Override
    public Connection<REQ, RES> getConnection() {
        return createConnection(tcpClientConfig);
    }

    /**
     * 创建连接
     *
     * @param tcpClientConfig
     * @return
     */
    protected abstract Connection<REQ, RES> createConnection(TcpClientConfig<REQ, RES> tcpClientConfig);

}
