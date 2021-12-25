package com.yiran.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class TcpClientBuilderTest {

    @Test
    public void open() throws IOException {
        TcpClientBuilder<String, String> builder = new TcpClientBuilder<String, String>()
                .type(TypeEnum.NETTY)
                .host("10.10.127.0")
                .port(14396)
                .readerIdleTime(30)
                .writerIdleTime(30)
                .soBackLog(1024)
                .tcpNoDelay(true)
                .soKeepAlive(true)
                .pool(true)
                .decoder(tcpByteBuf -> new String(tcpByteBuf.readBytes(10), StandardCharsets.UTF_8))
                .encoder((t, tcpByteBuf) -> {
                    tcpByteBuf.writeBytes(t.getBytes(StandardCharsets.UTF_8));
                    return true;
                })
                .heartFunc(() -> "hello world")
                .isHeart((res) -> res.length() == 10);
        TcpClient<String, String> tcpClient = builder.build();
        tcpClient.send("hello world");
    }

}