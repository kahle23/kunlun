package artoria.util;

import java.io.*;

import static artoria.util.StringConstant.DEFAULT_CHARSET_NAME;
import static artoria.util.StringConstant.ENDL;

/**
 * @author Kahle
 */
public class RuntimeUtils {

    public static Process doExec(String command)
            throws IOException {
        return Runtime.getRuntime().exec(command);
    }

    public static Process doExec(String[] cmdarray)
            throws IOException {
        return Runtime.getRuntime().exec(cmdarray);
    }

    public static Process doExec(String command, String[] envp)
            throws IOException {
        return Runtime.getRuntime().exec(command, envp);
    }

    public static Process doExec(String[] cmdarray, String[] envp)
            throws IOException {
        return Runtime.getRuntime().exec(cmdarray, envp);
    }

    public static Process doExec(String command, String[] envp, File dir)
            throws IOException {
        return Runtime.getRuntime().exec(command, envp, dir);
    }

    public static Process doExec(String[] cmdarray, String[] envp, File dir)
            throws IOException {
        return Runtime.getRuntime().exec(cmdarray, envp, dir);
    }

    public static String exec(Process process)
            throws IOException {
        return exec(process, DEFAULT_CHARSET_NAME);
    }

    public static String exec(Process process, String encoding)
            throws IOException {
        InputStream in = null;
        try {
            in = process.getInputStream();
            return IOUtils.toString(in, encoding);
        }
        finally {
            IOUtils.closeQuietly(in);
            process.destroy();
        }
    }

    public static String exec(Process process, long runtime)
            throws IOException {
        return exec(process, runtime, DEFAULT_CHARSET_NAME);
    }

    public static String exec(Process process, long runtime, String encoding)
            throws IOException {
        BufferedReader reader = null;
        try {
            InputStream i = process.getInputStream();
            InputStreamReader r = new InputStreamReader(i, encoding);
            reader = new BufferedReader(r);

            StringBuilder builder = new StringBuilder();
            long target = System.currentTimeMillis() + runtime;
            long current = System.currentTimeMillis();
            for (String line; (line = reader.readLine()) != null
                    && current < target; ) {
                builder.append(line).append(ENDL);
                current = System.currentTimeMillis();
            }
            return builder.toString();
        }
        finally {
            IOUtils.closeQuietly(reader);
            process.destroy();
        }
    }

    public static String exec(String command)
            throws IOException {
        return exec(command, DEFAULT_CHARSET_NAME);
    }

    public static String exec(String command, String encoding)
            throws IOException {
        return exec(doExec(command), encoding);
    }

    public static String exec(String command, long runtime)
            throws IOException {
        return exec(command, runtime, DEFAULT_CHARSET_NAME);
    }

    public static String exec(String command, long runtime, String encoding)
            throws IOException {
        return exec(doExec(command), runtime, encoding);
    }

}
