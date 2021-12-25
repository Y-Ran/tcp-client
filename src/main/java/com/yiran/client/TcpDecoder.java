package com.yiran.client;

import java.io.IOException;

/**
 * socket客户端解码器
 *
 * @author cuiyi
 * @version 1.0
 * @date 2020/4/16 14:23
 */
public interface TcpDecoder<RES> {

    /**
     * 解码输入字节流
     * 需要处理字节流不够解码对象
     *
     * @param tcpByteBuf 输入字节流
     * @return 如果当前字节流可以正常解码 返回解码后对象。如果当前字节不够解码 返回null
     * @throws IOException 如果解码异常 则抛出
     */
    RES decode(TcpByteBuf tcpByteBuf) throws IOException;

}
