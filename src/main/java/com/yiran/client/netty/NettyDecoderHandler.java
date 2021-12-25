package com.yiran.client.netty;

import com.yiran.client.TcpDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.function.Function;

public class NettyDecoderHandler<RES> extends ByteToMessageDecoder {

    private final TcpDecoder<RES> tcpDecoder;

    private final Function<ByteBuf, NettyTcpByteBuf> buildFunction = NettyTcpByteBuf::new;

    public NettyDecoderHandler(TcpDecoder<RES> tcpDecoder) {
        this.tcpDecoder = tcpDecoder;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        NettyTcpByteBuf nettyTcpByteBuf = buildFunction.apply(in);
        in.markReaderIndex();
        RES res = tcpDecoder.decode(nettyTcpByteBuf);
        if (res == null) {
            in.resetReaderIndex();
        } else {
            out.add(res);
        }
    }
}
