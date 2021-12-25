module com.yiran.client {
    requires java.base;

    requires org.slf4j;
    requires io.netty.handler;
    requires io.netty.buffer;
    requires io.netty.transport;
    requires io.netty.codec;
    requires org.apache.commons.pool2;

    exports com.yiran.client;

}