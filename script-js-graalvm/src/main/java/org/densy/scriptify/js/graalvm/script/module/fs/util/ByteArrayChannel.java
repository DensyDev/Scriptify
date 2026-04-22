package org.densy.scriptify.js.graalvm.script.module.fs.util;

import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class ByteArrayChannel implements SeekableByteChannel {

    private final byte[] data;
    private int position = 0;
    private boolean open = true;

    public ByteArrayChannel(byte[] data) {
        this.data = data;
    }

    @Override
    public int read(ByteBuffer dst) {
        if (position >= data.length) return -1;
        int toRead = Math.min(dst.remaining(), data.length - position);
        dst.put(data, position, toRead);
        position += toRead;
        return toRead;
    }

    @Override
    public SeekableByteChannel position(long pos) {
        position = (int) pos;
        return this;
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public long size() {
        return data.length;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void close() {
        open = false;
    }

    @Override
    public int write(ByteBuffer src) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SeekableByteChannel truncate(long size) {
        throw new UnsupportedOperationException();
    }
}
