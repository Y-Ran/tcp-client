package com.yiran.client;

import java.io.Closeable;
import java.io.IOException;

/**
 * 继承了Closeable接口 使用者需要close
 *
 * @param <REQ>
 * @param <RES>
 */
public interface Connection<REQ, RES> extends Closeable {

    /**
     * 发送并接受请求
     *
     * @param req
     * @return
     * @throws IOException
     */
    RES sendRequest(REQ req) throws IOException;

    /**
     * 建立连接
     *
     * @throws IOException
     */
    void connect() throws IOException;

    /**
     * 关闭连接
     *
     * @throws IOException
     */
    void close() throws IOException;

    /**
     * 判断连接是否已关闭
     *
     * @return 如果连接关闭 返回true
     */
    boolean isClosed();

}
