package com.yiran.client;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author cuiyi
 * @version 1.0
 * @date 2020/4/16 15:43
 */
public class TcpClientConfig<REQ, RES> {
    /**
     * 目标host
     */
    private String host = "127.0.0.1";
    /**
     * 目标端口
     */
    private int port = 4396;
    /**
     * 连接超时断开时间 读 秒，超时断开连接
     * 0表示不设置超时时间
     */
    private int readerIdleTime = 60;
    /**
     * 连接超时断开时间 写 秒，超时触发心跳
     * 0表示不设置超时时间
     */
    private int writerIdleTime = 60;
    /**
     * SO_BACKLOG
     */
    private int soBackLog = 1024;
    /**
     * TCP_NODELAY 是否关闭delay算法
     */
    private boolean tcpNoDelay = true;
    /**
     * SO_KEEPALIVE 是否保持长连接
     */
    private boolean soKeepAlive = true;

    /**
     * 获取心跳包
     */
    private Supplier<REQ> heartGet;
    /**
     * 响应是否为心跳包
     */
    private Predicate<RES> isHeartResponse;
    /**
     * 解码器
     */
    private TcpDecoder<RES> tcpDecoder;
    /**
     * 编码器
     *
     * @return
     */
    private TcpEncoder<REQ> tcpEncoder;

    TcpClientConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(int readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    public int getWriterIdleTime() {
        return writerIdleTime;
    }

    public void setWriterIdleTime(int writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }

    public int getSoBackLog() {
        return soBackLog;
    }

    public void setSoBackLog(int soBackLog) {
        this.soBackLog = soBackLog;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public boolean isSoKeepAlive() {
        return soKeepAlive;
    }

    public void setSoKeepAlive(boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
    }

    public Supplier<REQ> getHeartGet() {
        return heartGet;
    }

    public void setHeartGet(Supplier<REQ> heartGet) {
        this.heartGet = heartGet;
    }

    public Predicate<RES> getIsHeartResponse() {
        return isHeartResponse;
    }

    public void setIsHeartResponse(Predicate<RES> isHeartResponse) {
        this.isHeartResponse = isHeartResponse;
    }

    public TcpDecoder<RES> getTcpDecoder() {
        return tcpDecoder;
    }

    public void setTcpDecoder(TcpDecoder<RES> tcpDecoder) {
        this.tcpDecoder = tcpDecoder;
    }

    public TcpEncoder<REQ> getTcpEncoder() {
        return tcpEncoder;
    }

    public void setTcpEncoder(TcpEncoder<REQ> tcpEncoder) {
        this.tcpEncoder = tcpEncoder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcpClientConfig<?, ?> that = (TcpClientConfig<?, ?>) o;
        return port == that.port && readerIdleTime == that.readerIdleTime && writerIdleTime == that.writerIdleTime && soBackLog == that.soBackLog && tcpNoDelay == that.tcpNoDelay && soKeepAlive == that.soKeepAlive && Objects.equals(host, that.host) && Objects.equals(heartGet, that.heartGet) && Objects.equals(isHeartResponse, that.isHeartResponse) && Objects.equals(tcpDecoder, that.tcpDecoder) && Objects.equals(tcpEncoder, that.tcpEncoder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, readerIdleTime, writerIdleTime, soBackLog, tcpNoDelay, soKeepAlive, heartGet, isHeartResponse, tcpDecoder, tcpEncoder);
    }

    @Override
    public String toString() {
        return "TcpClientConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", readerIdleTime=" + readerIdleTime +
                ", writerIdleTime=" + writerIdleTime +
                ", soBackLog=" + soBackLog +
                ", tcpNoDelay=" + tcpNoDelay +
                ", soKeepAlive=" + soKeepAlive +
                ", heartGet=" + heartGet +
                ", isHeartResponse=" + isHeartResponse +
                ", tcpDecoder=" + tcpDecoder +
                ", tcpEncoder=" + tcpEncoder +
                '}';
    }
}
