/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.channels.Selector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The close tools.
 * @author Kahle
 */
public class CloseUtils {

    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            }
            catch (IOException ioe) {
                // ignored
            }
        }
    }

    public static void closeQuietly(Selector selector) {
        if (selector != null) {
            try {
                selector.close();
            }
            catch (IOException ioe) {
                // ignored
            }
        }
    }

    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            }
            catch (IOException ioe) {
                // ignored
            }
        }
    }

    public static void closeQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException se) {
                // ignore
            }
        }
    }

    public static void closeQuietly(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            }
            catch (SQLException se) {
                // ignore
            }
        }
    }

    public static void closeQuietly(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            }
            catch (SQLException se) {
                // ignore
            }
        }
    }

    public static void closeQuietly(URLConnection urlConnection) {
        if (urlConnection instanceof HttpURLConnection) {
            ((HttpURLConnection) urlConnection).disconnect();
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ioe) {
                // ignore
            }
        }
    }

//    TODO: 1.7
//    public static void closeQuietly(AutoCloseable autoCloseable) {
//        if (autoCloseable != null) {
//            try {
//                autoCloseable.close();
//            }
//            catch (Exception e) {
//                // ignore
//            }
//        }
//    }

}
