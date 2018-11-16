package artoria.net;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

public class NetUtilsTest {
    private static Logger log = LoggerFactory.getLogger(NetUtilsTest.class);
    private static String testIp0 = "www.bing.com";
    private static String testIp1 = "www.github.com";
    private static String testIp2 = "192.168.1.1";

    @Test
    @Ignore
    public void testTryTelnet() throws Exception {
        log.info("" + NetUtils.tryTelnet(testIp0, 80));
        log.info("" + NetUtils.tryTelnet(testIp1, 80));
        log.info("" + NetUtils.tryTelnet(testIp2, 80));
        log.info("" + NetUtils.tryTelnet(testIp0, 999));
    }

    @Test
    @Ignore
    public void testTryTelnet1() throws Exception {
        for (int i = 70; i < 90; i++) {
            log.info(i + " " + NetUtils.tryTelnet(testIp0, i, 500));
        }
    }

    @Test
    @Ignore
    public void testPing() throws Exception {
        // In windows, java maybe is not use ICMP, just echo(port 7).
        log.info("" + NetUtils.ping(testIp0));
        log.info("" + NetUtils.ping(testIp1));
        log.info("" + NetUtils.ping(testIp2));
    }

    @Test
    public void testGetLocalHost() throws Exception {
        InetAddress localHost = NetUtils.getLocalHost();
        log.info(localHost.toString());
        log.info(NetUtils.getHostAddress(localHost));
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
        log.info(NetUtils.getHardwareAddress(networkInterface));
    }

    @Test
    public void testGetNetworkInterfaces() throws Exception {
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface anInterface : interfaces) {
            log.info("" + anInterface);
            log.info(NetUtils.getHardwareAddress(anInterface));
            log.info("");
        }
    }

    @Test
    public void testGetInetAddresses() throws Exception {
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface networkInterface : interfaces) {
            log.info("" + NetUtils.getInetAddresses(networkInterface));
        }
    }

}
