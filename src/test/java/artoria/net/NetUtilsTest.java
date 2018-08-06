package artoria.net;

import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

public class NetUtilsTest {
    private static String testIp0 = "www.bing.com";
    private static String testIp1 = "www.github.com";
    private static String testIp2 = "192.168.1.1";

    @Test
    @Ignore
    public void testTryTelnet() throws Exception {
        System.out.println(NetUtils.tryTelnet(testIp0, 80));
        System.out.println(NetUtils.tryTelnet(testIp1, 80));
        System.out.println(NetUtils.tryTelnet(testIp2, 80));
        System.out.println(NetUtils.tryTelnet(testIp0, 999));
    }

    @Test
    @Ignore
    public void testTryTelnet1() throws Exception {
        for (int i = 70; i < 90; i++) {
            System.out.println(i + " " + NetUtils.tryTelnet(testIp0, i, 500));
        }
    }

    @Test
    @Ignore
    public void testPing() throws Exception {
        // In windows, java maybe is not use ICMP, just echo(port 7).
        System.out.println(NetUtils.ping(testIp0));
        System.out.println(NetUtils.ping(testIp1));
        System.out.println(NetUtils.ping(testIp2));
    }

    @Test
    public void testGetLocalHost() throws Exception {
        InetAddress localHost = NetUtils.getLocalHost();
        System.out.println(localHost.toString());
        System.out.println(NetUtils.getHostAddress(localHost));
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
        System.out.println(NetUtils.getHardwareAddress(networkInterface));
    }

    @Test
    public void testGetNetworkInterfaces() throws Exception {
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface anInterface : interfaces) {
            System.out.println(anInterface);
            System.out.println(NetUtils.getHardwareAddress(anInterface));
            System.out.println();
        }
    }

    @Test
    public void testGetInetAddresses() throws Exception {
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface networkInterface : interfaces) {
            System.out.println(NetUtils.getInetAddresses(networkInterface));
        }
    }

}
