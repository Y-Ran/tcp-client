package com.yiran.client;

import com.yiran.client.factory.ConnectionFactory;

import java.io.IOException;

public class TcpClient<REQ, RES> {

    private final ConnectionFactory<REQ, RES> connectionFactory;

    TcpClient(ConnectionFactory<REQ, RES> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * 发送请求
     *
     * @param request
     * @return
     */
    public RES send(REQ request) throws IOException {
        try (Connection<REQ, RES> connection = connectionFactory.getConnection()) {
            return connection.sendRequest(request);
        }
    }

}
