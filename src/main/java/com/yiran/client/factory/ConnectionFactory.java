package com.yiran.client.factory;

import com.yiran.client.Connection;

public interface ConnectionFactory<REQ, RES> {

    /**
     * 创建一个新的连接
     *
     * @return
     */
    Connection<REQ, RES> getConnection();

}
