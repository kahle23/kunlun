package artoria.net;

import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
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
    private static final Integer DEFAULT_PING_TIMEOUT = 1000;
    private static final Integer DEFAULT_TELNET_OPEN_TIMEOUT = 1000;

    public static boolean tryTelnet(String ip, int port) {

        return NetUtils.tryTelnet(ip, port, DEFAULT_TELNET_OPEN_TIMEOUT);
    }

    public static boolean tryTelnet(String ip, int port, int timeout) {
        SocketAddress address = new InetSocketAddress(ip, port);
        return NetUtils.tryTelnet(address, timeout);
    }

    public static boolean tryTelnet(SocketAddress address, int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(address, timeout);
            return true;
        }
        catch (IOException e) {
            return false;
        }
        finally {
            IOUtils.closeQuietly(socket);
        }
    }

    public static boolean ping(String ip) {

        return NetUtils.ping(ip, DEFAULT_PING_TIMEOUT);
    }

    public static boolean ping(String ip, int timeout) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(timeout);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static boolean ping(InetAddress in, int timeout) {
        try {
            return in.isReachable(timeout);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
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

    public static List<NetworkInterface> getNetworkInterfaces() {
        List<NetworkInterface> result = new ArrayList<NetworkInterface>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            CollectionUtils.addAll(result, interfaces);
        }
        catch (SocketException e) {
            throw ExceptionUtils.wrap(e);
        }
        return result;
    }

    public static List<InetAddress> getInetAddresses(NetworkInterface networkInterface) {
        Assert.notNull(networkInterface, "Parameter \"networkInterface\" must not null. ");
        List<InetAddress> result = new ArrayList<InetAddress>();
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        CollectionUtils.addAll(result, addresses);
        return result;
    }

    public static String getHostAddress(InetAddress inetAddress) {
        Assert.notNull(inetAddress, "Parameter \"inetAddress\" must not null. ");
        String address = inetAddress.getHostAddress();
        int index = address.indexOf(PERCENT_SIGN);
        return index != EOF ? address.substring(0, index) : address;
    }

    public static String getHardwareAddress(NetworkInterface networkInterface) {
        Assert.notNull(networkInterface, "Parameter \"networkInterface\" must not null. ");
        byte[] hAddress;
        try {
            hAddress = networkInterface.getHardwareAddress();
        }
        catch (SocketException e) {
            throw ExceptionUtils.wrap(e);
        }
        if (ArrayUtils.isEmpty(hAddress)) { return null; }
        int len = hAddress.length;
        Formatter formatter = new Formatter();
        for (int i = 0; i < len; i++) {
            String separate = i < (len - 1) ? MINUS : EMPTY_STRING;
            formatter.format("%02X%s", hAddress[i], separate);
        }
        return formatter.toString();
    }

}
