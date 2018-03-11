package com.github.kahlkn.artoria.net;

import com.github.kahlkn.artoria.exception.UncheckedException;
import com.github.kahlkn.artoria.io.IOUtils;
import com.github.kahlkn.artoria.util.ArrayUtils;
import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.CollectionUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;

/**
 * Net tools.
 * @author Kahle
 */
public class NetUtils {
    private static final Integer DEFAULT_PING_TIMEOUT = 1000;
    private static final Integer DEFAULT_TELNET_OPEN_TIMEOUT = 1000;

    public static boolean telnetOpen(String ip, int port) {
        return NetUtils.telnetOpen(ip, port, DEFAULT_TELNET_OPEN_TIMEOUT);
    }

    public static boolean telnetOpen(String ip, int port, int timeout) {
        SocketAddress address = new InetSocketAddress(ip, port);
        return NetUtils.telnetOpen(address, timeout);
    }

    public static boolean telnetOpen(SocketAddress address, int timeout) {
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
            return NetUtils.ping(InetAddress.getByName(ip), timeout);
        }
        catch (IOException e) {
            throw new UncheckedException(e);
        }
    }

    public static boolean ping(InetAddress in, int timeout) throws IOException {
        return in.isReachable(timeout);
    }

    public static List<String> findInet4Address() throws SocketException {
        List<String> result = new ArrayList<String>();
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface in : interfaces) {
            List<InetAddress> list = NetUtils.getInetAddresses(in);
            for (InetAddress address : list) {
                if (!(address instanceof Inet4Address)) {
                    continue;
                }
                String s = NetUtils.getHostAddress(address);
                result.add(s);
            }
        }
        return result;
    }

    public static List<String> findInet6Address() throws SocketException {
        List<String> result = new ArrayList<String>();
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface in : interfaces) {
            List<InetAddress> list = NetUtils.getInetAddresses(in);
            for (InetAddress address : list) {
                if (!(address instanceof Inet6Address)) {
                    continue;
                }
                String s = NetUtils.getHostAddress(address);
                result.add(s);
            }
        }
        return result;
    }

    public static InetAddress getLocalHost() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static List<NetworkInterface> getNetworkInterfaces() throws SocketException {
        List<NetworkInterface> result = new ArrayList<NetworkInterface>();
        Enumeration<NetworkInterface> ins = NetworkInterface.getNetworkInterfaces();
        CollectionUtils.addAll(result, ins);
        return result;
    }

    public static List<InetAddress> getInetAddresses(NetworkInterface in) {
        List<InetAddress> result = new ArrayList<InetAddress>();
        Enumeration<InetAddress> addresses = in.getInetAddresses();
        CollectionUtils.addAll(result, addresses);
        return result;
    }

    public static String getHostAddress(InetAddress in) {
        Assert.notNull(in, "Parameter \"InetAddress\" must not null. ");
        String address = in.getHostAddress();
        int index = address.indexOf("%");
        return index != -1 ? address.substring(0, index) : address;
    }

    public static String getHardwareAddress(NetworkInterface in) throws SocketException {
        byte[] hAddress = in.getHardwareAddress();
        if (ArrayUtils.isEmpty(hAddress)) {
            return null;
        }
        int len = hAddress.length;
        Formatter fm = new Formatter();
        for (int i = 0; i < len; i++) {
            String sp = i < (len - 1) ? "-" : "";
            fm.format("%02X%s", hAddress[i], sp);
        }
        return fm.toString();
    }

}
