/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net.http;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

/**
 * The http tools.
 * @author Kahle
 */
public class HttpUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
    private static volatile HttpProvider httpProvider;

    public static HttpProvider getHttpProvider() {
        if (httpProvider != null) { return httpProvider; }
        synchronized (HttpUtil.class) {
            if (httpProvider != null) { return httpProvider; }
            HttpUtil.setHttpProvider(new SimpleHttpProvider());
            return httpProvider;
        }
    }

    public static void setHttpProvider(HttpProvider httpProvider) {
        Assert.notNull(httpProvider, "Parameter \"httpProvider\" must not null. ");
        log.info("Set http provider: {}", httpProvider.getClass().getName());
        HttpUtil.httpProvider = httpProvider;
    }

    public static String getDefaultClientName() {

        return getHttpProvider().getDefaultClientName();
    }

    public static void setDefaultClientName(String defaultClientName) {

        getHttpProvider().setDefaultClientName(defaultClientName);
    }

    public static void registerClient(String name, HttpClient httpClient) {

        getHttpProvider().registerClient(name, httpClient);
    }

    public static void deregisterClient(String name) {

        getHttpProvider().deregisterClient(name);
    }

    public static HttpClient getHttpClient(String name) {

        return getHttpProvider().getHttpClient(name);
    }

    public static HttpResponse execute(HttpRequest request) {

        return getHttpProvider().execute(getDefaultClientName(), request);
    }

    public static HttpResponse execute(String name, HttpRequest request) {

        return getHttpProvider().execute(name, request);
    }

}
