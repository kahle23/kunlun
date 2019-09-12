package artoria.net;

import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.CloseUtils;
import artoria.util.CollectionUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;

import static artoria.common.Constants.*;
import static artoria.io.IOUtils.EOF;

/**
 * Net tools.
 * @author Kahle
 */
public class NetUtils {
    private static final Integer DEFAULT_REACHABLE_TIMEOUT = 1000;
    private static final Integer DEFAULT_CONNECTED_TIMEOUT = 1000;
    private static Logger log = LoggerFactory.getLogger(NetUtils.class);

    public static boolean reachable(String ipAddress) {

        return NetUtils.reachable(ipAddress, DEFAULT_REACHABLE_TIMEOUT);
    }

    public static boolean reachable(String ipAddress, int timeout) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            return inetAddress.isReachable(timeout);
        }
        catch (IOException e) {
            return false;
        }
    }

    public static boolean reachable(InetAddress inetAddress, int timeout) {
        try {
            return inetAddress.isReachable(timeout);
        }
        catch (IOException e) {
            return false;
        }
    }

    public static boolean connected(String ipAddress, int port) {

        return NetUtils.connected(ipAddress, port, DEFAULT_CONNECTED_TIMEOUT);
    }

    public static boolean connected(String ipAddress, int port, int timeout) {
        SocketAddress socketAddress = new InetSocketAddress(ipAddress, port);
        return NetUtils.connected(socketAddress, timeout);
    }

    public static boolean connected(SocketAddress socketAddress, int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(socketAddress, timeout);
            return socket.isConnected();
        }
        catch (IOException e) {
            return false;
        }
        finally {
            CloseUtils.closeQuietly(socket);
        }
    }

    public static InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        }
        catch (UnknownHostException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static String getHostAddress(InetAddress inetAddress) {
        Assert.notNull(inetAddress, "Parameter \"inetAddress\" must not null. ");
        String address = inetAddress.getHostAddress();
        int index = address.indexOf(PERCENT_SIGN);
        return index != EOF ? address.substring(0, index) : address;
    }

    public static String getHardwareAddress(NetworkInterface networkInterface) {
        Assert.notNull(networkInterface, "Parameter \"networkInterface\" must not null. ");
        byte[] hardwareAddress;
        try {
            hardwareAddress = networkInterface.getHardwareAddress();
        }
        catch (SocketException e) {
            throw ExceptionUtils.wrap(e);
        }
        if (ArrayUtils.isEmpty(hardwareAddress)) {
            return null;
        }
        int length = hardwareAddress.length;
        int lastIndex = length - 1;
        Formatter formatter = new Formatter();
        for (int i = 0; i < length; i++) {
            String separate = i < lastIndex ? MINUS : EMPTY_STRING;
            formatter.format("%02X%s", hardwareAddress[i], separate);
        }
        return formatter.toString();
    }

    public static List<NetworkInterface> getNetworkInterfaces() {
        try {
            List<NetworkInterface> result = new ArrayList<NetworkInterface>();
            Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();
            CollectionUtils.addAll(result, interfaces);
            return result;
        }
        catch (SocketException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static List<InetAddress> getInetAddresses(NetworkInterface networkInterface) {
        Assert.notNull(networkInterface, "Parameter \"networkInterface\" must not null. ");
        List<InetAddress> result = new ArrayList<InetAddress>();
        Enumeration<InetAddress> addresses =
                networkInterface.getInetAddresses();
        CollectionUtils.addAll(result, addresses);
        return result;
    }

}
