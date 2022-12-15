package artoria.io.file;

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

}
