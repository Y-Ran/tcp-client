package com.yiran.client;

public interface TcpByteBuf {
    /**
     * 返回当前字节流的可读字节数
     *
     * @return
     */
    int readableBytes();

    // 读方法

    boolean readBoolean();

    byte readByte();

    short readUnsignedByte();

    short readShort();

    short readShortLE();

    int readUnsignedShort();

    int readUnsignedShortLE();

    int readMedium();

    int readMediumLE();

    int readUnsignedMedium();

    int readUnsignedMediumLE();

    int readInt();

    int readIntLE();

    long readUnsignedInt();

    long readUnsignedIntLE();

    long readLong();

    long readLongLE();

    char readChar();

    float readFloat();

    float readFloatLE();

    double readDouble();

    double readDoubleLE();

    byte[] readBytes(int length);

    // 写方法

    TcpByteBuf writerBoolean(boolean value);

    TcpByteBuf writeByte(int value);

    TcpByteBuf writeShort(int value);

    TcpByteBuf writeShortLE(int value);

    TcpByteBuf writeMedium(int value);

    TcpByteBuf writeMediumLE(int value);

    TcpByteBuf writeInt(int value);

    TcpByteBuf writeIntLE(int value);

    TcpByteBuf writeLong(long value);

    TcpByteBuf writeLongLE(long value);

    TcpByteBuf writeChar(int value);

    TcpByteBuf writeFloat(float value);

    TcpByteBuf writeFloatLE(float value);

    TcpByteBuf writeDouble(double value);

    TcpByteBuf writeDoubleLE(double value);

    TcpByteBuf writeBytes(byte[] bytes);

    TcpByteBuf writeBytes(byte[] src, int srcIndex, int length);


}
