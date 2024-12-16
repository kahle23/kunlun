/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util.handler.support;

import kunlun.data.bean.BeanUtils;
import kunlun.renderer.RenderUtils;
import kunlun.util.handler.RenderHandler;

import java.util.Map;

/**
 * The template rendering tool default implementation.
 * @author Kahle
 */
public class RenderHandlerImpl implements RenderHandler {

    @Override
    public String render(String rendererName, Object template, String name, Object data) {
        Map<String, Object> dataMap = BeanUtils.beanToMap(data);
        return RenderUtils.renderToString(rendererName, template, name, dataMap);
    }

}
