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
     * Get filename extension.
     * @return Filename extension
     */
    String getExtension();

    /**
     * Set filename extension.
     * @param extension Filename extension
     */
    void setExtension(String extension);

    /**
     * Read from input stream.
     * @param inputStream The input stream that will be read
     * @return Read length
     * @throws IOException Maybe it will come up
     */
    long read(InputStream inputStream) throws IOException;

    /**
     * Write to output stream.
     * @param outputStream The output stream that will be write
     * @throws IOException Maybe it will come up
     */
    void write(OutputStream outputStream) throws IOException;

}
