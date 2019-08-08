package artoria.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * File entity.
 * @author Kahle
 */
public interface FileEntity {

    /**
     * Get file name.
     * @return The name of the file to get
     */
    String getName();

    /**
     * Set file name.
     * @param fileName The name of the file to set
     */
    void setName(String fileName);

    /**
     * Returns an input stream that reads from this file entity.
     * @return An input stream that reads from this file entity
     * @throws IOException If an I/O error occurs while creating the input stream
     */
    InputStream getInputStream() throws IOException;

    /**
     * Read from input stream.
     * @param inputStream The input stream that will be read
     * @return Read length
     * @throws IOException If an I/O error occurs while reading the input stream
     */
    long read(InputStream inputStream) throws IOException;

    /**
     * Write to output stream.
     * @param outputStream The output stream that will be write
     * @throws IOException If an I/O error occurs while writing the output stream
     */
    void write(OutputStream outputStream) throws IOException;

}
