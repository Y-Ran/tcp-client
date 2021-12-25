package com.yiran.client;

/**
 * socket客户端编码器
 *
 * @author cuiyi
 * @version 1.0
 * @date 2020/4/16 14:25
 */
public interface TcpEncoder<REQ> {

    /**
     * 编码
     *
     * @param t
     * @return
     */
    boolean encode(REQ t, TcpByteBuf tcpByteBuf);

}
