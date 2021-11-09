package io.sinistral.proteus.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DataOps {

    public static void writeStreamToPath(InputStream inputStream, Path path) throws IOException
    {

        try (ReadableByteChannel byteBufferByteChannel = Channels.newChannel(inputStream))
        {
            try (WritableByteChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE))
            {

                fastChannelCopy(byteBufferByteChannel, fileChannel);
            }
        }

    }

    public static ByteBuffer streamToBuffer(InputStream stream) throws IOException {

        final io.sinistral.proteus.utilities.AsyncByteOutputStream byteArrayOutputStream = new io.sinistral.proteus.utilities.AsyncByteOutputStream(65536);
        stream.transferTo(byteArrayOutputStream);
        return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
    }

    public static ByteBuffer readAllBytes(Path fp) throws IOException {

        try (final FileChannel fileChannel = FileChannel.open(fp, StandardOpenOption.READ))
        {
            final ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());

            fileChannel.read(buffer);

            buffer.flip();

            return buffer;
        }
    }

    public static void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel destination) throws IOException {

        final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

        while (src.read(buffer) != -1)
        {

            buffer.flip();

            destination.write(buffer);

            buffer.compact();
        }

        buffer.flip();

        while (buffer.hasRemaining())
        {
            destination.write(buffer);
        }
    }

}
