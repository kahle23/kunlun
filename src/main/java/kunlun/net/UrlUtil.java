/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net;

import kunlun.exception.ExceptionUtil;
import kunlun.util.StrUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL（Uniform Resource Locator）相关工具类.
 * @author Kahle
 */
public class UrlUtil {

    public static String completeUrl(String baseUrl, String relativeUri) {
        if (StrUtil.isBlank(baseUrl)) { return null; }
        try {
            URL completeUrl = new URL(new URL(baseUrl), relativeUri);
            return completeUrl.toString();
        } catch (MalformedURLException e) { throw ExceptionUtil.wrap(e); }
    }

}
