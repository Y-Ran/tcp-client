package com.yiran.client.netty;

import com.yiran.client.TcpByteBuf;
import io.netty.buffer.ByteBuf;

public class NettyTcpByteBuf implements TcpByteBuf {

    private final ByteBuf byteBuf;

    public NettyTcpByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    @Override
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return byteBuf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return byteBuf.readShort();
    }

    @Override
    public short readShortLE() {
        return byteBuf.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return byteBuf.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return byteBuf.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return byteBuf.readMedium();
    }

    @Override
    public int readMediumLE() {
        return byteBuf.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return byteBuf.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return byteBuf.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return byteBuf.readInt();
    }

    @Override
    public int readIntLE() {
        return byteBuf.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return byteBuf.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return byteBuf.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return byteBuf.readLong();
    }

    @Override
    public long readLongLE() {
        return byteBuf.readLongLE();
    }

    @Override
    public char readChar() {
        return byteBuf.readChar();
    }

    @Override
    public float readFloat() {
        return byteBuf.readFloat();
    }

    @Override
    public float readFloatLE() {
        return byteBuf.readFloatLE();
    }

    @Override
    public double readDouble() {
        return byteBuf.readDouble();
    }

    @Override
    public double readDoubleLE() {
        return byteBuf.readDoubleLE();
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] out = new byte[length];
        byteBuf.readBytes(out);
        return out;
    }

    @Override
    public TcpByteBuf writerBoolean(boolean value) {
        byteBuf.writeBoolean(value);
        return this;
    }

    @Override
    public TcpByteBuf writeByte(int value) {
        byteBuf.writeByte(value);
        return this;
    }

    @Override
    public TcpByteBuf writeShort(int value) {
        byteBuf.writeShort(value);
        return this;
    }

    @Override
    public TcpByteBuf writeShortLE(int value) {
        byteBuf.writeShortLE(value);
        return this;
    }

    @Override
    public TcpByteBuf writeMedium(int value) {
        byteBuf.writeMedium(value);
        return this;
    }

    @Override
    public TcpByteBuf writeMediumLE(int value) {
        byteBuf.writeMediumLE(value);
        return this;
    }

    @Override
    public TcpByteBuf writeInt(int value) {
        byteBuf.writeInt(value);
        return this;
    }

    @Override
    public TcpByteBuf writeIntLE(int value) {
        byteBuf.writeIntLE(value);
        return this;
    }

    @Override
    public TcpByteBuf writeLong(long value) {
        byteBuf.writeLong(value);
        return this;
    }

    @Override
    public TcpByteBuf writeLongLE(long value) {
        byteBuf.writeLongLE(value);
        return this;
    }

    @Override
    public TcpByteBuf writeChar(int value) {
        byteBuf.writeChar(value);
        return this;
    }

    @Override
    public TcpByteBuf writeFloat(float value) {
        byteBuf.writeFloat(value);
        return this;
    }

    @Override
    public TcpByteBuf writeFloatLE(float value) {
        byteBuf.writeFloatLE(value);
        return this;
    }

    @Override
    public TcpByteBuf writeDouble(double value) {
        byteBuf.writeDouble(value);
        return this;
    }

    @Override
    public TcpByteBuf writeDoubleLE(double value) {
        byteBuf.writeDoubleLE(value);
        return this;
    }

    @Override
    public TcpByteBuf writeBytes(byte[] bytes) {
        byteBuf.writeBytes(bytes);
        return this;
    }

    @Override
    public TcpByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        byteBuf.writeBytes(src, srcIndex, length);
        return this;
    }
}
