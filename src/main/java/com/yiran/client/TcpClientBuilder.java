package com.yiran.client;

import com.yiran.client.factory.ConnectionFactory;
import com.yiran.client.factory.NormalConnectionFactory;
import com.yiran.client.factory.PoolConfig;
import com.yiran.client.factory.PoolConnectionFactory;
import com.yiran.client.netty.NettyConnectionImpl;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class TcpClientBuilder<REQ, RES> {

    private final TcpClientConfig<REQ, RES> tcpClientConfig;

    /**
     * 实现类型
     * <p>
     * netty
     * socket
     */
    private TypeEnum type = TypeEnum.NETTY;

    private boolean pool;

    private int maxIdle = 8;

    private int minIdle = 4;

    private int maxTotal = 8;

    private int maxWaitMillis = -1;

    private boolean testOnBorrow = true;

    private boolean testWhileIdle = true;

    public TcpClientBuilder() {
        this.tcpClientConfig = new TcpClientConfig<>();
    }

    /**
     * @param reqClass 请求包class
     * @param resClass 响应包class
     * @param <NREQ>   请求包类型
     * @param <NRES>   响应包类型
     * @return
     */
    public static <NREQ, NRES> TcpClientBuilder<NREQ, NRES> open(Class<NREQ> reqClass, Class<NRES> resClass) {
        return new TcpClientBuilder<>();
    }

    /**
     * 构建tcp客户端
     *
     * @return
     */
    public TcpClient<REQ, RES> build() {
        ConnectionFactory<REQ, RES> connectionFactory = null;
        switch (this.type) {
            case NETTY:
                connectionFactory = new NormalConnectionFactory<>(tcpClientConfig, NettyConnectionImpl::new);
                break;
            case SOCKET:
                throw new UnsupportedOperationException("不支持当前类型 socket...");
            default:
        }
        if (pool) {
            PoolConfig poolConfig = new PoolConfig();
            poolConfig.setMaxIdle(maxIdle);
            poolConfig.setMinIdle(minIdle);
            poolConfig.setMaxTotal(maxTotal);
            poolConfig.setMaxWaitMillis(maxWaitMillis);
            poolConfig.setTestOnBorrow(testOnBorrow);
            poolConfig.setTestWhileIdle(testWhileIdle);
            connectionFactory = new PoolConnectionFactory<>(connectionFactory, poolConfig);
        }
        return new TcpClient<>(connectionFactory);
    }

    /**
     * 连接实现类型
     *
     * @param type
     * @return
     */
    public TcpClientBuilder<REQ, RES> type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * 目标host
     *
     * @param host
     * @return
     */
    public TcpClientBuilder<REQ, RES> host(String host) {
        tcpClientConfig.setHost(host);
        return this;
    }

    /**
     * 目标端口
     *
     * @param port
     * @return
     */
    public TcpClientBuilder<REQ, RES> port(int port) {
        tcpClientConfig.setPort(port);
        return this;
    }

    /**
     * 读超时时间 秒
     * 超时断开连接
     * 0表示不设置超时时间
     *
     * @param readerIdleTime
     * @return
     */
    public TcpClientBuilder<REQ, RES> readerIdleTime(int readerIdleTime) {
        tcpClientConfig.setReaderIdleTime(readerIdleTime);
        return this;
    }

    /**
     * 写超时时间 秒
     * 超时触发心跳
     * 0表示不设置超时时间
     *
     * @param writerIdleTime
     * @return
     */
    public TcpClientBuilder<REQ, RES> writerIdleTime(int writerIdleTime) {
        tcpClientConfig.setWriterIdleTime(writerIdleTime);
        return this;
    }

    /**
     * SO_BACKLOG
     *
     * @param soBackLog
     * @return
     */
    public TcpClientBuilder<REQ, RES> soBackLog(int soBackLog) {
        tcpClientConfig.setSoBackLog(soBackLog);
        return this;
    }

    /**
     * TCP_NODELAY 是否关闭delay算法
     *
     * @param tcpNoDelay
     * @return
     */
    public TcpClientBuilder<REQ, RES> tcpNoDelay(boolean tcpNoDelay) {
        tcpClientConfig.setTcpNoDelay(tcpNoDelay);
        return this;
    }

    /**
     * SO_KEEPALIVE 是否保持长连接
     *
     * @param soKeepAlive
     * @return
     */
    public TcpClientBuilder<REQ, RES> soKeepAlive(boolean soKeepAlive) {
        tcpClientConfig.setSoKeepAlive(soKeepAlive);
        return this;
    }

    /**
     * 是否使用连接池
     *
     * @param pool
     * @return
     */
    public TcpClientBuilder<REQ, RES> pool(boolean pool) {
        this.pool = pool;
        return this;
    }

    /**
     * 解码器
     *
     * @param decoder
     * @return
     */
    public TcpClientBuilder<REQ, RES> decoder(TcpDecoder<RES> decoder) {
        tcpClientConfig.setTcpDecoder(decoder);
        return this;
    }

    /**
     * 编码器
     *
     * @param encoder
     * @return
     */
    public TcpClientBuilder<REQ, RES> encoder(TcpEncoder<REQ> encoder) {
        tcpClientConfig.setTcpEncoder(encoder);
        return this;
    }

    /**
     * 获取心跳包
     *
     * @param heartFunc
     * @return
     */
    public TcpClientBuilder<REQ, RES> heartFunc(Supplier<REQ> heartFunc) {
        tcpClientConfig.setHeartGet(heartFunc);
        return this;
    }

    /**
     * 响应是否为心跳包
     *
     * @param isHeartFunc
     * @return
     */
    public TcpClientBuilder<REQ, RES> isHeart(Predicate<RES> isHeartFunc) {
        tcpClientConfig.setIsHeartResponse(isHeartFunc);
        return this;
    }


}
