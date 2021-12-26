package com.yiran.client.netty;

import com.yiran.client.Connection;
import com.yiran.client.TcpClientConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NettyConnectionImpl<REQ, RES> extends SimpleChannelInboundHandler<RES> implements Connection<REQ, RES> {
    public static final Logger LOGGER = LoggerFactory.getLogger(NettyConnectionImpl.class);
    /**
     * 目标地址
     */
    private final InetSocketAddress serverAddress;
    /**
     * 读超时时间
     */
    private final long readTimeoutMill;
    /**
     * 写超时时间
     */
    private final long writerTimeoutMill;

    /**
     * SO_BACKLOG
     */
    private final int soBackLog;
    /**
     * TCP_NODELAY 是否关闭delay算法
     */
    private final boolean tcpNoDelay;
    /**
     * SO_KEEPALIVE 是否保持长连接
     */
    private final boolean soKeepAlive;
    /**
     * 请求编码器
     */
    private final NettyEncoderHandler<REQ> encoderHandler;
    /**
     * 响应解码器
     */
    private final NettyDecoderHandler<RES> decoderHandler;
    /**
     * channel
     */
    private volatile Channel channel;
    /**
     * 事件循环
     */
    private volatile EventLoopGroup eventLoopGroup;
    /**
     * 获取心跳包
     */
    private final Supplier<REQ> getHeartPackageFunc;
    /**
     * 判断响应是否为心跳包
     */
    private final Predicate<RES> isHeartResponse;
    /**
     * 响应
     */
    private volatile RES response;
    /**
     * 异常
     */
    private Exception exception;

    public NettyConnectionImpl(TcpClientConfig<REQ, RES> tcpClientConfig) {
        this.serverAddress = new InetSocketAddress(tcpClientConfig.getHost(), tcpClientConfig.getPort());
        this.readTimeoutMill = tcpClientConfig.getReaderIdleTime() * 1000L;
        this.writerTimeoutMill = tcpClientConfig.getWriterIdleTime() * 1000L;
        this.getHeartPackageFunc = tcpClientConfig.getHeartGet();
        this.isHeartResponse = tcpClientConfig.getIsHeartResponse();
        this.soBackLog = tcpClientConfig.getSoBackLog();
        this.tcpNoDelay = tcpClientConfig.isTcpNoDelay();
        this.soKeepAlive = tcpClientConfig.isSoKeepAlive();
        this.encoderHandler = new NettyEncoderHandler<>(tcpClientConfig.getTcpEncoder());
        this.decoderHandler = new NettyDecoderHandler<>(tcpClientConfig.getTcpDecoder());
    }

    @Override
    public RES sendRequest(REQ request) throws IOException {
        if (isClosed()) {
            throw new IOException("connect is closed");
        }
        try {
            ChannelFuture writeFuture = this.channel.writeAndFlush(request);
            if (!writeFuture.await(readTimeoutMill)) {
                close();
                throw new IOException(writeFuture.cause());
            }
            LOGGER.debug("request已经发送... id={}, request={}", getChannelRoute(this.channel), request.toString());
            RES resp = getResponse();
            Exception exception = getException();
            if (exception != null) {
                LOGGER.error("connection send request error ... request={}", request);
                LOGGER.error("", exception);
                throw new IOException(exception);
            }
            LOGGER.debug("response响应... id={}, response={}", getChannelRoute(channel), resp);
            return resp;
        } catch (InterruptedException | IOException e) {
            LOGGER.error("connection send request error ... request={}", request);
            LOGGER.error("", e);
            reConnect();
            throw new IOException(e);
        }
    }

    @Override
    public void connect() throws IOException {
        if (!isClosed()) {
            return;
        }
        if (this.channel != null) {
            close();
        }
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, soBackLog)
                .option(ChannelOption.TCP_NODELAY, tcpNoDelay)
                // 长连接
                .option(ChannelOption.SO_KEEPALIVE, soKeepAlive)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", decoderHandler);
                        pipeline.addLast("encoder", encoderHandler);
                        // 超时检测放到解码器后可以保证是解码成功才记录正常数据的超时判断，防止垃圾数据长连接在线
                        pipeline.addLast(new IdleStateHandler(readTimeoutMill, writerTimeoutMill, 0, TimeUnit.MILLISECONDS));
                        pipeline.addLast("handler", this);
                    }
                });
        ChannelFuture channelFuture;
        try {
            channelFuture = bootstrap.connect(serverAddress).sync();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }

        if (!channelFuture.awaitUninterruptibly().isSuccess()) {
            group.shutdownGracefully();
            throw new IOException(channelFuture.cause());
        }
        this.channel = channelFuture.channel();
        this.eventLoopGroup = group;
        LOGGER.debug("connect success !!!");
    }

    @Override
    public void close() throws IOException {
        Exception exception = new IOException("connection close ...");
        if (this.channel != null) {
            this.channel.disconnect();
            this.channel.close().awaitUninterruptibly();
            this.channel = null;
        }
        if (this.eventLoopGroup != null) {
            this.eventLoopGroup.shutdownGracefully();
            this.eventLoopGroup = null;
        }
        LOGGER.warn("connection close ... ", exception);
    }

    @Override
    public boolean isClosed() {
        return this.channel == null || !this.channel.isOpen() || !this.channel.isActive();
    }

    /**
     * 重建连接
     *
     * @throws Exception
     */
    private void reConnect() throws IOException {
        LOGGER.warn("reconnect ... ");
        this.close();
        this.connect();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RES msg) throws Exception {
        if (isHeartResponse.test(msg)) {
            LOGGER.debug("客户端接收心跳包 id={} heart={}", getChannelRoute(ctx.channel()), msg);
        } else {
            setResponse(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                if (getHeartPackageFunc != null) {
                    Object heartPackage = getHeartPackageFunc.get();
                    LOGGER.debug("客户端定时心跳包 id={}, heart={}",
                            getChannelRoute(ctx.channel()), heartPackage);
                    ctx.writeAndFlush(heartPackage);
                }
            } else if (event.state() == IdleState.READER_IDLE) {
                LOGGER.debug("客户端读超时,连接断开... timeout={}s, id={}",
                        readTimeoutMill, getChannelRoute(ctx.channel()));
                throw new IOException("connection read idle ... timeout=" + readTimeoutMill + "s" +
                        ", id=" + getChannelRoute(ctx.channel()));
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("====客户端建立连接," + getChannelRoute(ctx.channel()));
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("====客户端连接中断," + getChannelRoute(ctx.channel()));
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("", cause);
        setException(new Exception(cause));
    }

    private synchronized RES getResponse() throws IOException {
        long deadlineTime = System.currentTimeMillis() + readTimeoutMill;
        for (long currtime = System.currentTimeMillis(); currtime < deadlineTime; currtime = System.currentTimeMillis()) {
            try {
                this.wait(deadlineTime - System.currentTimeMillis());
                if (response != null) {
                    break;
                }
            } catch (InterruptedException e) {
                if (exception != null) {
                    throw new IOException(exception);
                }
                throw new IOException("请求等待响应被中断...", e);
            }
        }
        RES res = response;
        response = null;
        return res;
    }

    private synchronized void setResponse(RES response) {
        this.response = response;
        this.notify();
    }

    private synchronized Exception getException() {
        return exception;
    }

    private synchronized void setException(Exception exception) {
        this.exception = exception;
        this.notify();
    }

    private static InetSocketAddress getLocalAddress(Channel channel) {
        return (InetSocketAddress) channel.localAddress();
    }

    private static InetSocketAddress getRemoteAddress(Channel channel) {
        return (InetSocketAddress) channel.remoteAddress();
    }

    private static String getChannelRoute(Channel channel) {
        return getLocalAddress(channel) + " -> " + getRemoteAddress(channel);
    }
}
