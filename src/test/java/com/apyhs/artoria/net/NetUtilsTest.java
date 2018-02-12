package com.apyhs.artoria.net;

import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

public class NetUtilsTest {
    private static String testIp0 = "www.baidu.com";
    private static String testIp1 = "www.taobao.com";
    private static String testIp2 = "www.sohu.com";
    private static String testIp3 = "192.168.1.1";
    private static String testIp4 = "www.baidu.com";

    @Test
    @Ignore
    public void testTelnetOpen() throws Exception {
        System.out.println(NetUtils.telnetOpen(testIp0, 80));
        System.out.println(NetUtils.telnetOpen(testIp1, 80));
        System.out.println(NetUtils.telnetOpen(testIp2, 80));
        System.out.println(NetUtils.telnetOpen(testIp3, 80));
        System.out.println(NetUtils.telnetOpen(testIp4, 999));
    }

    @Test
    @Ignore
    public void testTelnetOpen1() throws Exception {
        for (int i = 70; i < 90; i++) {
            System.out.println(i + " " + NetUtils.telnetOpen(testIp0, i, 500));
        }
    }

    @Test
    @Ignore
    public void testPing() throws Exception {
        System.out.println(NetUtils.ping(testIp0));
        System.out.println(NetUtils.ping(testIp1));
        System.out.println(NetUtils.ping(testIp2));
        System.out.println(NetUtils.ping(testIp3));
        System.out.println(NetUtils.ping(testIp4));
    }

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
