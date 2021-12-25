package com.yiran.client.netty;

import com.yiran.client.TcpEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoderHandler<REQ> extends MessageToByteEncoder<REQ> {

    private final TcpEncoder<REQ> tcpEncoder;

    public NettyEncoderHandler(TcpEncoder<REQ> tcpEncoder) {
        this.tcpEncoder = tcpEncoder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, REQ msg, ByteBuf out) throws Exception {
        NettyTcpByteBuf nettyTcpByteBuf = new NettyTcpByteBuf(out);
        tcpEncoder.encode(msg, nettyTcpByteBuf);
    }
}
