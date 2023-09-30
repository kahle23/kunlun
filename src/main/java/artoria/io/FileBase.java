package artoria.io;

import artoria.core.Resource;

/**
 * The base information about the file.
 * @author Kahle
 */
public interface FileBase extends Resource {

    /**
     * Get the file name.
     * @return The file name
     */
    String getName();

    /**
     * Get the file path.
     * @return The file path
     */
    String getPath();

    /**
     * Get the file charset (nullable).
     * @return The file charset (nullable)
     */
    String getCharset();

}
