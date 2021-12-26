package com.yiran.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class TcpClientBuilderTest {

    @Test
    public void open() throws IOException {
        TcpClientBuilder<Person, Result> builder = new TcpClientBuilder<Person, Result>()
                .type(TypeEnum.NETTY)
                .host("10.10.127.0")
                .port(14396)
                .readerIdleTime(30)
                .writerIdleTime(30)
                .soBackLog(1024)
                .tcpNoDelay(true)
                .soKeepAlive(true)
                .pool(false)
                .decoder(tcpByteBuf -> {
                    String result = new String(tcpByteBuf.readBytes(10), StandardCharsets.UTF_8);
                    Result result1 = new Result();
                    result1.setResult(result);
                    return result1;
                })
                .encoder((t, tcpByteBuf) -> {
                    tcpByteBuf.writeBytes(t.getName().getBytes(StandardCharsets.UTF_8));
                    tcpByteBuf.writeInt(t.getAge());
                    return true;
                })
                .heartFunc(() -> {
                    Person person = new Person();
                    person.setName("test");
                    return person;
                })
                .isHeart((res) -> true);
        TcpClient<Person, Result> tcpClient = builder.build();
        tcpClient.send(new Person());
    }

    @Test
    public void test2() {
        Config<String> config = new Config<>();
        config.print = (str) -> str;
        Wrap<String> wrap = new Wrap<>();
        System.out.println(wrap.create(config).print());
    }

}