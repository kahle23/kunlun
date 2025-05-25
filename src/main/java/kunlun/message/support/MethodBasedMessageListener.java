/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.support;

import kunlun.exception.ExceptionUtil;
import kunlun.message.MessageListener;
import kunlun.message.model.Message;

import java.lang.reflect.Method;

import static kunlun.util.Assert.notNull;

/**
 * MethodBasedMessageListener
 * @author Kahle
 */
public class MethodBasedMessageListener implements MessageListener {
    private final Method targetMethod;
    private final Object targetBean;

    public MethodBasedMessageListener(Method targetMethod, Object targetBean) {
        this.targetMethod = notNull(targetMethod);
        this.targetBean = targetBean;
    }

    public Method getTargetMethod() {

        return targetMethod;
    }

    public Object getTargetBean() {

        return targetBean;
    }

    @Override
    public Object onMessage(Message message) {
        try {
            return targetMethod.invoke(targetBean, message);
        } catch (Exception e) {
            throw ExceptionUtil.wrap(e);
        }
    }
}
