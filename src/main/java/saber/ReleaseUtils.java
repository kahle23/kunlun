package saber;

import java.net.HttpURLConnection;

public abstract class ReleaseUtils {

    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Exception e) {
                // Quietly
            }
        }
    }

    public static void destroyQuietly(Process process) {
        if (process != null) {
            process.destroy();
        }
    }

    public static void disconnectQuietly(HttpURLConnection conn) {
        if (conn != null) {
            conn.disconnect();
        }
    }

}
