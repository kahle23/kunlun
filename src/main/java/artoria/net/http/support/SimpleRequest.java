package artoria.net.http.support;

import artoria.data.KeyValue;
import artoria.data.KeyValueImpl;
import artoria.net.http.HttpMethod;
import artoria.net.http.HttpRequest;
import artoria.util.Assert;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static artoria.common.Constants.ZERO;

/**
 * The simple http request.
 * @author Kahle
 */
public class SimpleRequest extends AbstractHttpBase implements HttpRequest {
    private final Collection<KeyValue<String, Object>> parameters = new ArrayList<KeyValue<String, Object>>();
    private Object body;
    private Proxy proxy;
    /**
     * timeout value in milliseconds.
     */
    private Integer readTimeout = 19000;
    /**
     * timeout value in milliseconds.
     */
    private Integer connectTimeout = 19000;

    public SimpleRequest(String url, HttpMethod method) {
        Assert.notNull(method, "Parameter \"method\" must not null. ");
        Assert.notBlank(url, "Parameter \"url\" must not blank. ");
        this.setMethod(method);
        this.setUrl(url);
    }

    public SimpleRequest() {

    }

    public Proxy getProxy() {

        return proxy;
    }

    public void setProxy(Proxy proxy) {
        Assert.notNull(proxy, "Parameter \"proxy\" must not null. ");
        this.proxy = proxy;
    }

    public void setProxy(String hostname, int port) {
        Assert.notBlank(hostname, "Parameter \"hostname\" must not blank. ");
        Assert.isTrue(port > ZERO, "Parameter \"port\" must greater than 0. ");
        SocketAddress address = new InetSocketAddress(hostname, port);
        this.proxy = new Proxy(Proxy.Type.HTTP, address);
    }

    public Integer getConnectTimeout() {

        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        Assert.notNull(connectTimeout, "Parameter \"connectTimeout\" must not null. ");
        Assert.isTrue(connectTimeout > ZERO, "Parameter \"connectTimeout\" must greater than 0. ");
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {

        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        Assert.notNull(readTimeout, "Parameter \"readTimeout\" must not null. ");
        Assert.isTrue(readTimeout > ZERO, "Parameter \"readTimeout\" must greater than 0. ");
        this.readTimeout = readTimeout;
    }

    public Object getParameter(String paramName) {
        Assert.notBlank(paramName, "Parameter \"paramName\" must not blank. ");
        for (KeyValue<String, Object> parameter : parameters) {
            if (paramName.equals(parameter.getKey())) {
                return parameter.getValue();
            }
        }
        return null;
    }

    public void addParameter(String paramName, Object paraValue) {
        Assert.notBlank(paramName, "Parameter \"paramName\" must not blank. ");
        parameters.add(new KeyValueImpl<String, Object>(paramName, paraValue));
    }

    public void addParameters(Collection<KeyValue<String, Object>> parameters) {
        Assert.notEmpty(parameters, "Parameter \"parameters\" must not empty. ");
        this.parameters.addAll(parameters);
    }

    public void addParameters(Map<?, ?> parameters) {
        Assert.notEmpty(parameters, "Parameter \"parameters\" must not empty. ");
        for (Map.Entry<?, ?> entry : parameters.entrySet()) {
            String key   = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            this.parameters.add(new KeyValueImpl<String, Object>(key, value));
        }
    }

    @Override
    public Collection<KeyValue<String, Object>> getParameters() {

        return Collections.unmodifiableCollection(parameters);
    }

    public void clearParameters() {

        parameters.clear();
    }

    @Override
    public Object getBody() {

        return body;
    }

    public void setBody(Object body) {

        this.body = body;
    }

}
