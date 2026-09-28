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

/**
 * Static byte and stream copy utilities used by request extraction and file handling.
 *
 * @author jbauer
 */
public class DataUtilities {

    private static final Logger logger = LoggerFactory.getLogger(DataUtilities.class.getName());

    /** Default constructor for static-only use. */
    public DataUtilities() {}

    /**
     * Copies a stream to a file path with a direct-buffer channel copy.
     *
     * @param inputStream the source stream
     * @param path the destination path
     * @throws IOException when a read or write fails
     */
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

    /**
     * Reads a stream fully into a heap byte buffer.
     *
     * @param stream the source stream
     * @return a buffer holding every remaining byte of the stream
     * @throws IOException when a read fails
     */
    public static ByteBuffer streamToBuffer(InputStream stream) throws IOException {

        return ByteBuffer.wrap(stream.readAllBytes());
    }

    /**
     * Reads a form file item into a byte buffer, from memory or its backing file.
     *
     * @param fileItem the uploaded file item
     * @return a buffer holding the file content
     * @throws Exception when the content cannot be read
     */
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

    /**
     * Reads an entire file into a heap byte buffer sized to the file.
     *
     * @param fp the file to read
     * @return a buffer holding the file content
     * @throws IOException when the file cannot be opened or read
     */
    public static ByteBuffer readAllBytes(Path fp) throws IOException {

        try (final FileChannel fileChannel = FileChannel.open(fp, StandardOpenOption.READ))
        {
            final ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());

            fileChannel.read(buffer);

            buffer.flip();

            return buffer;
        }
    }


    /**
     * Copies between channels with a shared direct buffer.
     *
     * @param src the source channel
     * @param destination the destination channel
     * @throws IOException when a read or write fails
     */
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
