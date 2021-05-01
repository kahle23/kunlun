package artoria.net;

import artoria.lang.KeyValue;
import artoria.lang.KeyValuePair;
import artoria.util.Assert;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static artoria.common.Constants.ZERO;

/**
 * Http request message.
 * @author Kahle
 */
public class HttpRequest extends HttpMessage {
    private static final String USER_AGENT = "User-Agent";
    private boolean validateTLSCertificate = false;
    private boolean ignoreHttpError = true;
    private boolean followRedirect = true;
    private int connectTimeout = 19000;
    private int readTimeout = 19000;
    private Proxy proxy;
    private Collection<KeyValue<String, Object>> parameters = new ArrayList<KeyValue<String, Object>>();
    private Object body;
    private File responseBodyToFile;
    private OutputStream responseBodyToStream;
    private Writer responseBodyToWriter;

    {
        addHeader(USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
    }

    public HttpRequest() {
    }

    public boolean getValidateTLSCertificate() {

        return validateTLSCertificate;
    }

    public void setValidateTLSCertificate(boolean validateTLSCertificate) {

        this.validateTLSCertificate = validateTLSCertificate;
    }

    public boolean getIgnoreHttpError() {

        return ignoreHttpError;
    }

    public void setIgnoreHttpError(boolean ignoreHttpError) {

        this.ignoreHttpError = ignoreHttpError;
    }

    public boolean getFollowRedirect() {

        return followRedirect;
    }

    public void setFollowRedirect(boolean followRedirect) {

        this.followRedirect = followRedirect;
    }

    public int getConnectTimeout() {

        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        Assert.state(connectTimeout > ZERO, "Parameter \"connectTimeout\" must greater than 0. ");
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {

        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        Assert.state(readTimeout > ZERO, "Parameter \"readTimeout\" must greater than 0. ");
        this.readTimeout = readTimeout;
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
        Assert.state(port > ZERO, "Parameter \"port\" must greater than 0. ");
        SocketAddress address = new InetSocketAddress(hostname, port);
        this.proxy = new Proxy(Proxy.Type.HTTP, address);
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
        Assert.notNull(paraValue, "Parameter \"paraValue\" must not null. ");
        parameters.add(new KeyValuePair<String, Object>(paramName, paraValue));
    }

    public void addParameters(Collection<KeyValuePair<String, Object>> parameters) {
        Assert.notEmpty(parameters, "Parameter \"parameters\" must not empty. ");
        this.parameters.addAll(parameters);
    }

    public void addParameters(Map<String, Object> parameters) {
        Assert.notEmpty(parameters, "Parameter \"parameters\" must not empty. ");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            this.parameters.add(new KeyValuePair<String, Object>(key, value));
        }
    }

    public Collection<KeyValue<String, Object>> getParameters() {

        return Collections.unmodifiableCollection(parameters);
    }

    public void clearParameters() {

        parameters.clear();
    }

    public Object getBody() {

        return body;
    }

    public void setBody(Object body) {

        this.body = body;
    }

    public File getResponseBodyToFile() {

        return responseBodyToFile;
    }

    public void setResponseBodyToFile(File file) {
        Assert.notNull(file, "Parameter \"file\" must not null. ");
        this.responseBodyToFile = file;
    }

    public OutputStream getResponseBodyToStream() {

        return responseBodyToStream;
    }

    public void setResponseBodyToStream(OutputStream outputStream) {
        Assert.notNull(outputStream, "Parameter \"outputStream\" must not null. ");
        this.responseBodyToStream = outputStream;
    }

    public Writer getResponseBodyToWriter() {

        return responseBodyToWriter;
    }

    public void setResponseBodyToWriter(Writer writer) {
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        this.responseBodyToWriter = writer;
    }

}
