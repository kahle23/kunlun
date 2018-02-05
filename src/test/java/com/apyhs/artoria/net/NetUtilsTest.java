package com.apyhs.artoria.net;

import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

public class NetUtilsTest {

    @Test
    public void testTelnetOpen() throws Exception {
        System.out.println(NetUtils.telnetOpen("www.baidu.com", 80));
        System.out.println(NetUtils.telnetOpen("www.taobao.com", 80));
        System.out.println(NetUtils.telnetOpen("www.sohu.com", 80));
        System.out.println(NetUtils.telnetOpen("192.168.1.1", 80));
        System.out.println(NetUtils.telnetOpen("www.baidu.com", 999));
    }

    @Test
    public void testTelnetOpen1() throws Exception {
        for (int i = 0; i < 1024; i++) {
            System.out.println(i + " " + NetUtils.telnetOpen("www.baidu.com", i, 500));
        }
    }

    @Test
    public void testPing() throws Exception {
        System.out.println(NetUtils.ping("192.168.1.1"));
        System.out.println(NetUtils.ping("192.168.98.253"));
        System.out.println(NetUtils.ping("111.13.100.92"));
        System.out.println(NetUtils.ping("www.baidu.com"));
        System.out.println(NetUtils.ping("www.taobao.com"));
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
