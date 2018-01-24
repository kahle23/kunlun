package com.apyhs.artoria.util;

import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

public class NetUtilsTest {

    @Test
    public void testGetNetworkInterfaces() throws Exception {
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface in : interfaces) {
            System.out.println(in);
        }
    }

    @Test
    public void testGetHardwareAddress() throws Exception {
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface in : interfaces) {
            System.out.println(NetUtils.getHardwareAddress(in));
        }
    }

    @Test
    public void testGetLocalHost() throws Exception {
        InetAddress localHost = NetUtils.getLocalHost();
        NetworkInterface in = NetworkInterface.getByInetAddress(localHost);
        System.out.println(NetUtils.getHostAddress(localHost));
        System.out.println(NetUtils.getHardwareAddress(in));
    }

    @Test
    public void testGetInetAddresses() throws Exception {
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface in : interfaces) {
            System.out.println(NetUtils.getInetAddresses(in));
        }
    }

    @Test
    public void testFindInet4Address() throws Exception {
        List<String> inet4Address = NetUtils.findInet4Address();
        for (String address : inet4Address) {
            System.out.println(address);
        }
    }

    @Test
    public void testFindInet6Address() throws Exception {
        List<String> inet4Address = NetUtils.findInet6Address();
        for (String address : inet4Address) {
            System.out.println(address);
        }
    }

}
