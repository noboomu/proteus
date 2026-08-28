package io.sinistral.proteus.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class AsyncByteOutputStreamTest {

    @Test
    public void sizeTracksWrittenBytesRatherThanBufferCapacity() {
        AsyncByteOutputStream output = new AsyncByteOutputStream();

        assertEquals(0, output.size());

        byte[] value = "proteus".getBytes(StandardCharsets.UTF_8);
        output.write(value, 0, value.length);

        assertEquals(value.length, output.size());
    }
}
