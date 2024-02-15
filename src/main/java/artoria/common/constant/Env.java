package artoria.common.constant;

import artoria.io.util.FilenameUtils;
import artoria.net.NetUtils;

/**
 * The common environment constants.
 * @author Kahle
 */
public class Env {

    public static final String COMPUTER_NAME = System.getenv("ComputerName");
    public static final String HOST_NAME = NetUtils.getHostName();
    public static final String ROOT_PATH = FilenameUtils.getRootPath();
    public static final String CLASSPATH = FilenameUtils.getClasspath();

    private Env() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
