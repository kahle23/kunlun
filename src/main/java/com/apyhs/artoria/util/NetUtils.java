package com.apyhs.artoria.util;

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
