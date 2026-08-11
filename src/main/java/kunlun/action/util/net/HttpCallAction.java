package kunlun.action.util.net;

import kunlun.core.Action;
import kunlun.core.function.Function;
import kunlun.net.UrlUtil;
import kunlun.net.http.HttpResponse;
import kunlun.net.http.HttpUtil;
import kunlun.net.http.support.SimpleRequest;
import kunlun.net.http.support.SimpleResponse;
import kunlun.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.util.Assert.notNull;

/**
 * Http Api 调用器.
 * @author Kahle
 */
public abstract class HttpCallAction implements Action {
    protected static final String KEY_AUTHORIZATION = "Authorization";
    protected static final String KEY_USERNAME = "username";
    protected static final String KEY_PASSWORD = "password";
    private final Map<String, Function<HttpCallContext, Object>> httpProcessors;

    public HttpCallAction(Map<String, Function<HttpCallContext, Object>> httpProcessors) {

        this.httpProcessors = notNull(httpProcessors);
    }

    public HttpCallAction() {

        this(new ConcurrentHashMap<String, Function<HttpCallContext, Object>>());
    }

    public void registerProcessor(String strategy, Function<HttpCallContext, Object> processor) {

        httpProcessors.put(notNull(strategy), notNull(processor));
    }

    public void deregisterProcessor(String strategy) {

        httpProcessors.remove(notNull(strategy));
    }

    public Function<HttpCallContext, Object> getProcessor(String strategy) {

        return httpProcessors.get(notNull(strategy));
    }

    /**
     * 构建上下文对象.
     * @return 上下文对象
     */
    protected abstract HttpCallContext buildContext(String strategy, Object input, Object[] arguments);

    /**
     * 填充配置信息.
     * @param context 上下文对象
     */
    protected abstract void fillConfig(HttpCallContext context);

    /**
     * 填充 Token 信息.
     * @param context 上下文对象
     */
    protected abstract void fillToken(HttpCallContext context);

    /**
     * 在策略调用前的预处理.
     * @param context 上下文对象
     */
    protected void preProcess(HttpCallContext context) {
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        HttpCallContext ctx = buildContext(strategy, input, arguments);
        fillConfig(ctx);
        fillToken(ctx);
        preProcess(ctx);
        return getProcessor(ctx.getStrategy()).apply(ctx);
    }

    /**
     * Http 调用上下文.
     * @author Kahle
     */
    public static class HttpCallContext extends StrategyContext {
        private String urlPrefix;
        private String uri;
        private String authToken;

        private SimpleRequest httpRequest;
        private SimpleResponse httpResponse;

        public HttpCallContext(String strategy, Object input, Object[] arguments) {
            this.setStrategy(strategy);
            this.setInput(input);
            this.setArguments(arguments);
        }

        public HttpCallContext() {

        }

        public String getUrlPrefix() {

            return urlPrefix;
        }

        public void setUrlPrefix(String urlPrefix) {

            this.urlPrefix = urlPrefix;
        }

        public String getUri() {

            return uri;
        }

        public void setUri(String uri) {

            this.uri = uri;
        }

        public String getAuthToken() {

            return authToken;
        }

        public void setAuthToken(String authToken) {

            this.authToken = authToken;
        }

        public SimpleRequest getHttpRequest() {

            return httpRequest;
        }

        public void setHttpRequest(SimpleRequest httpRequest) {

            this.httpRequest = httpRequest;
        }

        public SimpleResponse getHttpResponse() {

            return httpResponse;
        }

        public void setHttpResponse(SimpleResponse httpResponse) {

            this.httpResponse = httpResponse;
        }

        /**
         * 进行 http 调用.
         */
        protected void doHttp() {
            SimpleRequest request = notNull(getHttpRequest());
            if (StrUtil.isBlank(request.getUrl())) {
                request.setUrl(UrlUtil.completeUrl(getUrlPrefix(), getUri()));
            }
            HttpResponse response = HttpUtil.execute(request);
            setHttpResponse((SimpleResponse) response);
        }
    }

}
