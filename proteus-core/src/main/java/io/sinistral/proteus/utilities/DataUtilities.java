package io.sinistral.proteus.utilities;

import com.typesafe.config.Config;
import io.undertow.server.handlers.form.FormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class DataUtilities {

    private static final Logger logger = LoggerFactory.getLogger(DataUtilities.class.getName());

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

        return ByteBuffer.wrap(stream.readAllBytes());
    }

    public static ByteBuffer fileItemToBuffer(FormData.FileItem fileItem) throws Exception
    {

        if (fileItem.isInMemory())
        {
            return ByteBuffer.wrap(fileItem.getInputStream().readAllBytes());
        }
        else
        {
            return readAllBytes(fileItem.getFile());
        }

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
