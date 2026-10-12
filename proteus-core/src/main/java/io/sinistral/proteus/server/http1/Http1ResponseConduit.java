package io.sinistral.proteus.server.http1;

import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.Connectors;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import org.xnio.Buffers;
import org.xnio.IoUtils;
import org.xnio.XnioWorker;
import org.xnio.channels.StreamSourceChannel;
import org.xnio.conduits.AbstractStreamSinkConduit;
import org.xnio.conduits.ConduitWritableByteChannel;
import org.xnio.conduits.Conduits;
import org.xnio.conduits.StreamSinkConduit;

/**
 * Writes the HTTP/1.1 response head ahead of the first body bytes.
 *
 * <p>The status line and headers are serialized once into a per-connection byte array, then
 * copied into a pooled buffer and written together with the first body chunk in a single
 * gathering write. Heads larger than one pooled buffer are drained across writes by offset.
 * Reason phrases come from a static table unless the exchange sets one explicitly.
 */
final class Http1ResponseConduit extends AbstractStreamSinkConduit<StreamSinkConduit> {

    private static final int STATE_BODY = 0;
    private static final int STATE_HEAD_PENDING = 1;
    private static final int STATE_HEAD_FLUSHING = 2;
    private static final int FLAG_SHUTDOWN = 1 << 4;
    private static final int MASK_STATE = 0xF;

    private static final byte[] HTTP11 = "HTTP/1.1 ".getBytes();
    private static final byte[] CRLF = { '\r', '\n' };
    private static final byte[] COLON_SP = { ':', ' ' };
    private static final byte[][] STATUS_LINES = new byte[600][];

    private final ByteBufferPool pool;
    private final Http1ServerConnection connection;

    private int state = STATE_HEAD_PENDING;
    private HttpServerExchange exchange;
    private PooledByteBuffer pooledBuffer;
    private PooledByteBuffer fileTransferBuffer;
    private ByteBuffer[] writev;
    private boolean done;

    /** Serialized head; grown to the largest head seen on this connection. */
    private byte[] head = new byte[512];
    private int headLen;
    private int headOffset;

    Http1ResponseConduit(StreamSinkConduit next, ByteBufferPool pool, Http1ServerConnection connection) {
        super(next);
        this.pool = pool;
        this.connection = connection;
    }

    void reset(HttpServerExchange exchange) {
        this.exchange = exchange;
        this.state = STATE_HEAD_PENDING;
        this.headLen = 0;
        this.headOffset = 0;
    }

    void freeBuffers() {
        done = true;
        if (pooledBuffer != null) {
            pooledBuffer.close();
            pooledBuffer = null;
        }
        if (fileTransferBuffer != null) {
            fileTransferBuffer.close();
            fileTransferBuffer = null;
        }
        exchange = null;
    }

    private static byte[] statusLine(int code) {
        if (code >= 100 && code < STATUS_LINES.length) {
            byte[] cached = STATUS_LINES[code];
            if (cached != null) {
                return cached;
            }
            String reason = StatusCodes.getReason(code);
            byte[] line = (code + " " + reason + "\r\n").getBytes();
            STATUS_LINES[code] = line;
            return line;
        }
        return (code + " " + StatusCodes.getReason(code) + "\r\n").getBytes();
    }

    private void ensureHead(int extra) {
        if (headLen + extra > head.length) {
            head = Arrays.copyOf(head, Math.max(head.length * 2, headLen + extra));
        }
    }

    private void append(byte[] b) {
        ensureHead(b.length);
        System.arraycopy(b, 0, head, headLen, b.length);
        headLen += b.length;
    }

    private void append(HttpString s) {
        int n = s.length();
        ensureHead(n);
        for (int i = 0; i < n; i++) {
            head[headLen++] = s.byteAt(i);
        }
    }

    private void appendLatin1(String s) {
        int n = s.length();
        ensureHead(n);
        for (int i = 0; i < n; i++) {
            head[headLen++] = (byte) s.charAt(i);
        }
    }

    /** Serializes status line and all response headers into {@link #head}. */
    private void buildHead() {
        Connectors.flattenCookies(exchange);
        headLen = 0;
        headOffset = 0;
        append(HTTP11);
        int code = exchange.getStatusCode();
        String reason = exchange.getReasonPhrase();
        if (reason == null) {
            append(statusLine(code));
        } else {
            appendLatin1(Integer.toString(code));
            head[headLen++] = ' ';
            appendLatin1(reason);
            append(CRLF);
        }
        HeaderMap headers = exchange.getResponseHeaders();
        long c = headers.fastIterateNonEmpty();
        while (c != -1L) {
            HeaderValues hv = headers.fiCurrent(c);
            HttpString name = hv.getHeaderName();
            int size = hv.size();
            for (int i = 0; i < size; i++) {
                append(name);
                append(COLON_SP);
                appendLatin1(hv.get(i));
                append(CRLF);
            }
            c = headers.fiNextNonEmpty(c);
        }
        append(CRLF);
    }

    /**
     * Writes pending head bytes, optionally gathering {@code userData} behind them.
     *
     * @return the new state: {@link #STATE_BODY} once the head is fully on the wire
     */
    private int processWrite(int state, Object userData, int pos, int length) throws IOException {
        if (done || exchange == null) {
            throw new ClosedChannelException();
        }
        if (state == STATE_HEAD_PENDING) {
            buildHead();
            if (pooledBuffer == null) {
                pooledBuffer = pool.allocate();
            }
            fill(pooledBuffer.getBuffer());
            state = STATE_HEAD_FLUSHING;
        }
        ByteBuffer buffer = pooledBuffer.getBuffer();
        while (true) {
            if (!buffer.hasRemaining()) {
                fill(buffer);
            }
            boolean headDone = headOffset == headLen;
            long res;
            if (userData == null || !headDone) {
                res = next.write(buffer);
            } else if (userData instanceof ByteBuffer) {
                ByteBuffer[] data = writev;
                if (data == null) {
                    data = writev = new ByteBuffer[2];
                }
                data[0] = buffer;
                data[1] = (ByteBuffer) userData;
                res = next.write(data, 0, 2);
            } else {
                ByteBuffer[] srcs = (ByteBuffer[]) userData;
                ByteBuffer[] data = writev;
                if (data == null || data.length < length + 1) {
                    data = writev = new ByteBuffer[length + 1];
                }
                data[0] = buffer;
                System.arraycopy(srcs, pos, data, 1, length);
                res = next.write(data, 0, length + 1);
            }
            if (res == 0) {
                return STATE_HEAD_FLUSHING;
            }
            if (!buffer.hasRemaining() && headDone) {
                bufferDone();
                return STATE_BODY;
            }
        }
    }

    /** Copies the next slice of the serialized head into the buffer and flips it for writing. */
    private void fill(ByteBuffer buffer) {
        buffer.clear();
        int n = Math.min(buffer.remaining(), headLen - headOffset);
        buffer.put(head, headOffset, n);
        headOffset += n;
        buffer.flip();
    }

    private void bufferDone() {
        if (pooledBuffer == null) {
            return;
        }
        if (connection.getExtraBytes() != null && connection.isOpen() && exchange != null && exchange.isRequestComplete()) {
            // pipelining: keep the buffer for the next response head
            pooledBuffer.getBuffer().clear();
        } else {
            pooledBuffer.close();
            pooledBuffer = null;
        }
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        try {
            int oldState = this.state;
            int state = oldState & MASK_STATE;
            int alreadyWritten = 0;
            int originalRemaining = -1;
            try {
                if (state != STATE_BODY) {
                    originalRemaining = src.remaining();
                    state = processWrite(state, src, -1, -1);
                    if (state != STATE_BODY) {
                        return 0;
                    }
                    alreadyWritten = originalRemaining - src.remaining();
                    if ((oldState & FLAG_SHUTDOWN) != 0) {
                        next.terminateWrites();
                        throw new ClosedChannelException();
                    }
                }
                if (alreadyWritten != originalRemaining) {
                    return next.write(src) + alreadyWritten;
                }
                return alreadyWritten;
            } finally {
                this.state = oldState & ~MASK_STATE | state;
            }
        } catch (IOException | RuntimeException | Error e) {
            IoUtils.safeClose(connection);
            throw e;
        }
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        if (length == 0) {
            return 0L;
        }
        int oldState = this.state;
        int state = oldState & MASK_STATE;
        try {
            if (state != STATE_BODY) {
                long rem = Buffers.remaining(srcs, offset, length);
                state = processWrite(state, srcs, offset, length);
                long ret = rem - Buffers.remaining(srcs, offset, length);
                if (state != STATE_BODY) {
                    return ret;
                }
                if ((oldState & FLAG_SHUTDOWN) != 0) {
                    next.terminateWrites();
                    throw new ClosedChannelException();
                }
                if (ret == rem) {
                    return ret;
                }
                return next.write(srcs, offset, length) + ret;
            }
            return next.write(srcs, offset, length);
        } catch (IOException | RuntimeException | Error e) {
            IoUtils.safeClose(connection);
            throw e;
        } finally {
            this.state = oldState & ~MASK_STATE | state;
        }
    }

    @Override
    public int writeFinal(ByteBuffer src) throws IOException {
        try {
            return Conduits.writeFinalBasic(this, src);
        } catch (IOException | RuntimeException | Error e) {
            IoUtils.safeClose(connection);
            throw e;
        }
    }

    @Override
    public long writeFinal(ByteBuffer[] srcs, int offset, int length) throws IOException {
        try {
            return Conduits.writeFinalBasic(this, srcs, offset, length);
        } catch (IOException | RuntimeException | Error e) {
            IoUtils.safeClose(connection);
            throw e;
        }
    }

    @Override
    public long transferFrom(FileChannel src, long position, long count) throws IOException {
        try {
            if ((state & MASK_STATE) != STATE_BODY) {
                PooledByteBuffer pooled = fileTransferBuffer != null ? fileTransferBuffer : pool.allocate();
                fileTransferBuffer = null;
                ByteBuffer buffer = pooled.getBuffer();
                try {
                    if (buffer.hasRemaining()) {
                        return write(buffer);
                    }
                    buffer.clear();
                    int res = src.read(buffer);
                    buffer.flip();
                    if (res <= 0) {
                        return res;
                    }
                    return write(buffer);
                } finally {
                    if (buffer.hasRemaining()) {
                        fileTransferBuffer = pooled;
                    } else {
                        pooled.close();
                    }
                }
            }
            return next.transferFrom(src, position, count);
        } catch (IOException | RuntimeException | Error e) {
            IoUtils.safeClose(connection);
            throw e;
        }
    }

    @Override
    public long transferFrom(StreamSourceChannel source, long count, ByteBuffer throughBuffer) throws IOException {
        try {
            if ((state & MASK_STATE) != STATE_BODY) {
                return IoUtils.transfer(source, count, throughBuffer, new ConduitWritableByteChannel(this));
            }
            return next.transferFrom(source, count, throughBuffer);
        } catch (IOException | RuntimeException | Error e) {
            IoUtils.safeClose(connection);
            throw e;
        }
    }

    @Override
    public boolean flush() throws IOException {
        int oldState = this.state;
        int state = oldState & MASK_STATE;
        try {
            if (state != STATE_BODY) {
                state = processWrite(state, null, -1, -1);
                if (state != STATE_BODY) {
                    return false;
                }
                if ((oldState & FLAG_SHUTDOWN) != 0) {
                    next.terminateWrites();
                }
            }
            return next.flush();
        } catch (IOException | RuntimeException | Error e) {
            IoUtils.safeClose(connection);
            throw e;
        } finally {
            this.state = oldState & ~MASK_STATE | state;
        }
    }

    @Override
    public void terminateWrites() throws IOException {
        try {
            int oldState = this.state;
            if ((oldState & MASK_STATE) == STATE_BODY) {
                next.terminateWrites();
                return;
            }
            this.state = oldState | FLAG_SHUTDOWN;
        } catch (IOException | RuntimeException | Error e) {
            IoUtils.safeClose(connection);
            throw e;
        }
    }

    @Override
    public void truncateWrites() throws IOException {
        try {
            next.truncateWrites();
        } catch (IOException | RuntimeException | Error e) {
            IoUtils.safeClose(connection);
            throw e;
        } finally {
            if (pooledBuffer != null) {
                bufferDone();
            }
            if (fileTransferBuffer != null) {
                fileTransferBuffer.close();
                fileTransferBuffer = null;
            }
        }
    }

    @Override
    public XnioWorker getWorker() {
        return next.getWorker();
    }
}
