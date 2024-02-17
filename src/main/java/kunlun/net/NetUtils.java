/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net;

import kunlun.exception.ExceptionUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.ArrayUtils;
import kunlun.util.Assert;
import kunlun.util.CloseUtils;
import kunlun.util.CollectionUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.*;
import static kunlun.io.util.IOUtils.EOF;

/**
 * The net tools.
 * @author Kahle
 */
public class NetUtils {
    private static final Integer DEFAULT_REACHABLE_TIMEOUT = 1000;
    private static final Integer DEFAULT_CONNECTED_TIMEOUT = 1000;
    private static final Logger log = LoggerFactory.getLogger(NetUtils.class);

    public static boolean reachable(String ipAddress) {

        return NetUtils.reachable(ipAddress, DEFAULT_REACHABLE_TIMEOUT);
    }

    public static boolean reachable(String ipAddress, int timeout) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            return inetAddress.isReachable(timeout);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static boolean reachable(InetAddress inetAddress, int timeout) {
        try {
            return inetAddress.isReachable(timeout);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
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
        catch (SocketTimeoutException e) {
            return false;
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(socket);
        }
    }

    public static String getHostName() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            if (localHost != null) {
                return localHost.getHostName();
            }
        }
        catch (Exception e) {
            log.info("The host name get failed. ", e);
        }
        return null;
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
        return index != EOF ? address.substring(ZERO, index) : address;
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
        int lastIndex = length - ONE;
        Formatter formatter = new Formatter();
        for (int i = ZERO; i < length; i++) {
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
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        List<InetAddress> result = new ArrayList<InetAddress>();
        CollectionUtils.addAll(result, addresses);
        return result;
    }

}
