package artoria.net;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

import static artoria.common.constant.Numbers.*;

public class NetUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(NetUtilsTest.class);
    private static final String testIp0 = "www.bing.com";
    private static final String testIp1 = "www.github.com";
    private static final String testIp2 = "192.168.1.1";

    @Test
    @Ignore
    public void testConnected() throws Exception {
        log.info("{}", NetUtils.connected(testIp0, EIGHTY));
        log.info("{}", NetUtils.connected(testIp1, EIGHTY));
        log.info("{}", NetUtils.connected(testIp2, EIGHTY));
        log.info("{}", NetUtils.connected(testIp0, NINE_HUNDRED_NINETY_NINE));
    }

    @Test
    @Ignore
    public void testConnected1() throws Exception {
        for (int i = SEVENTY; i < NINETY; i++) {
            log.info("{} {}", i, NetUtils.connected(testIp0, i, FIVE_HUNDRED));
        }
    }

    @Test
    @Ignore
    public void testReachable() throws Exception {
        // In windows, java maybe is not use ICMP, just echo(port 7).
        log.info("{}", NetUtils.reachable(testIp0));
        log.info("{}", NetUtils.reachable(testIp1));
        log.info("{}", NetUtils.reachable(testIp2));
    }

    @Test
    public void testGetHostName() throws Exception {
        log.info("{}", NetUtils.getHostName());
        log.info("{}", NetUtils.getHostName());
        log.info("{}", NetUtils.getHostName());
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
            log.info("{}", anInterface);
            log.info(NetUtils.getHardwareAddress(anInterface));
        }
    }

    @Test
    public void testGetInetAddresses() throws Exception {
        List<NetworkInterface> interfaces = NetUtils.getNetworkInterfaces();
        for (NetworkInterface networkInterface : interfaces) {
            List<InetAddress> addresses = NetUtils.getInetAddresses(networkInterface);
            if (CollectionUtils.isEmpty(addresses)) { continue; }
            for (InetAddress address : addresses) {
                log.info("{}", address.getHostAddress());
            }
            log.info("---- ----");
        }
    }

}
