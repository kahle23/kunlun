/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.CollUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

import static kunlun.common.constant.Numbers.*;

public class NetUtilTest {
    private static final Logger log = LoggerFactory.getLogger(NetUtilTest.class);
    private static final String testIp0 = "www.bing.com";
    private static final String testIp1 = "www.github.com";
    private static final String testIp2 = "192.168.1.1";

    @Test
    @Ignore
    public void testConnected() throws Exception {
        log.info("{}", NetUtil.connected(testIp0, EIGHTY));
        log.info("{}", NetUtil.connected(testIp1, EIGHTY));
        log.info("{}", NetUtil.connected(testIp2, EIGHTY));
        log.info("{}", NetUtil.connected(testIp0, NINE_HUNDRED_NINETY_NINE));
    }

    @Test
    @Ignore
    public void testConnected1() throws Exception {
        for (int i = SEVENTY; i < NINETY; i++) {
            log.info("{} {}", i, NetUtil.connected(testIp0, i, FIVE_HUNDRED));
        }
    }

    @Test
    @Ignore
    public void testReachable() throws Exception {
        // In windows, java maybe is not use ICMP, just echo(port 7).
        log.info("{}", NetUtil.reachable(testIp0));
        log.info("{}", NetUtil.reachable(testIp1));
        log.info("{}", NetUtil.reachable(testIp2));
    }

    @Test
    public void testGetHostName() throws Exception {
        log.info("{}", NetUtil.getHostName());
        log.info("{}", NetUtil.getHostName());
        log.info("{}", NetUtil.getHostName());
    }

    @Test
    public void testGetLocalHost() throws Exception {
        InetAddress localHost = NetUtil.getLocalHost();
        log.info(localHost.toString());
        log.info(NetUtil.getHostAddress(localHost));
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
        log.info(NetUtil.getHardwareAddress(networkInterface));
    }

    @Test
    public void testGetNetworkInterfaces() throws Exception {
        List<NetworkInterface> interfaces = NetUtil.getNetworkInterfaces();
        for (NetworkInterface anInterface : interfaces) {
            log.info("{}", anInterface);
            log.info(NetUtil.getHardwareAddress(anInterface));
        }
    }

    @Test
    public void testGetInetAddresses() throws Exception {
        List<NetworkInterface> interfaces = NetUtil.getNetworkInterfaces();
        for (NetworkInterface networkInterface : interfaces) {
            List<InetAddress> addresses = NetUtil.getInetAddresses(networkInterface);
            if (CollUtil.isEmpty(addresses)) { continue; }
            for (InetAddress address : addresses) {
                log.info("{}", address.getHostAddress());
            }
            log.info("---- ----");
        }
    }

}
