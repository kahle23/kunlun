package saber;

import java.io.*;
import java.nio.charset.Charset;

public abstract class RuntimeUtils {
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();

    public static Process exec(String command)
            throws IOException {
        return Runtime.getRuntime().exec(command);
    }

    public static Process exec(String[] cmdarray)
            throws IOException {
        return Runtime.getRuntime().exec(cmdarray);
    }

    public static Process exec(String command, String[] envp)
            throws IOException {
        return Runtime.getRuntime().exec(command, envp);
    }

    public static Process exec(String[] cmdarray, String[] envp)
            throws IOException {
        return Runtime.getRuntime().exec(cmdarray, envp);
    }

    public static Process exec(String command, String[] envp, File dir)
            throws IOException {
        return Runtime.getRuntime().exec(command, envp, dir);
    }

    public static Process exec(String[] cmdarray, String[] envp, File dir)
            throws IOException {
        return Runtime.getRuntime().exec(cmdarray, envp, dir);
    }

    public static String run(Process process)
            throws IOException {
        return run(process, DEFAULT_CHARSET_NAME);
    }

    public static String run(Process process, String encoding)
            throws IOException {
        InputStream in = null;
        try {
            in = process.getInputStream();
            return IOUtils.toString(in, encoding);
        } finally {
            ReleaseUtils.closeQuietly(in);
            ReleaseUtils.destroyQuietly(process);
        }
    }

    public static String run(Process process, long runtime)
            throws IOException {
        return run(process, runtime, DEFAULT_CHARSET_NAME);
    }

    public static String run(Process process, long runtime, String encoding)
            throws IOException {
        long target = System.currentTimeMillis() + runtime;
        BufferedReader reader = null;
        try {
            InputStream i = process.getInputStream();
            InputStreamReader r = new InputStreamReader(i, encoding);
            reader = new BufferedReader(r);
            StringBuilder builder = new StringBuilder();
            String line = null;
            long current = System.currentTimeMillis();
            while ((line = reader.readLine()) != null && current < target) {
                builder.append(line).append(StringUtils.ENDL);
                current = System.currentTimeMillis();
            }
            return builder.toString();
        } finally {
            ReleaseUtils.closeQuietly(reader);
            ReleaseUtils.destroyQuietly(process);
        }
    }

    public static String run(String command)
            throws IOException {
        return run(command, DEFAULT_CHARSET_NAME);
    }

    public static String run(String command, String encoding)
            throws IOException {
        return run(exec(command), encoding);
    }

    public static String run(String command, long runtime)
            throws IOException {
        return run(command, runtime, DEFAULT_CHARSET_NAME);
    }

    public static String run(String command, long runtime, String encoding)
            throws IOException {
        return run(exec(command), runtime, encoding);
    }

}
