package artoria.net.http;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The http provider simple implement by jdk.
 * @author Kahle
 */
public class SimpleHttpProvider implements HttpProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleHttpProvider.class);
    protected final Map<String, HttpClient> clients;
    protected final Map<String, Object> commonProperties;

    protected SimpleHttpProvider(Map<String, Object> commonProperties,
                                Map<String, HttpClient> clients) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(clients, "Parameter \"clients\" must not null. ");
        this.commonProperties = commonProperties;
        this.clients = clients;
    }

    public SimpleHttpProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, HttpClient>());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerClient(String name, HttpClient httpClient) {
        Assert.notNull(httpClient, "Parameter \"httpClient\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = httpClient.getClass().getName();
        httpClient.setCommonProperties(getCommonProperties());
        clients.put(name, httpClient);
        log.info("Register the http client \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterClient(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        HttpClient remove = clients.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the http client \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public HttpClient getHttpClient(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return clients.get(name);
    }

    @Override
    public HttpResponse execute(String name, HttpRequest request) {
        HttpClient httpClient = getHttpClient(name);
        Assert.notNull(httpClient
                , "The corresponding http client could not be found by name. ");
        return httpClient.execute(request);
    }

}
