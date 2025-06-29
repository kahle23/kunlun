/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.support;

import kunlun.exception.ExceptionUtil;
import kunlun.message.MessageListener;
import kunlun.message.model.Message;

import java.lang.reflect.InvocationTargetException;
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
        } catch (InvocationTargetException e) {
            // 该异常包裹了原始异常，需要提取一下
            throw ExceptionUtil.wrap(e.getTargetException());
        } catch (Exception e) {
            // 其他异常，直接抛出（进行了运行异常的转换）
            throw ExceptionUtil.wrap(e);
        }
    }
}
