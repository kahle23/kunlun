/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security;

import kunlun.bean.BeanHolder;
import kunlun.core.AccessController;
import kunlun.core.DataController;
import kunlun.util.Assert;
import kunlun.util.IteratorUtils;

import java.util.Collection;
import java.util.Map;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.security.TokenManager.Token;
import static kunlun.security.UserManager.UserDetail;

/**
 * The security tools.
 * @author Kahle
 */
public class SecurityUtils {

    public static SecurityContext getContext() {
        Map<String, SecurityContext> beans = BeanHolder.getBeans(SecurityContext.class);
        Assert.notEmpty(beans, "Please set the security context first. ");
        boolean isOne = beans.size() == ONE;
        Assert.isTrue(isOne, "The security context is limited to one. ");
        SecurityContext context = IteratorUtils.getFirst(beans.values());
        Assert.notNull(context, "The security context is null. ");
        return context;
    }

    // ====

    public static String getTraceId() {

        return getContext().getTraceId();
    }

    public static String getToken() {

        return getContext().getToken();
    }

    public static Object getUserId() {

        return getContext().getUserId();
    }

    public static <T> T getUserId(T defaultUserId) {
        Object userId = getContext().getUserId();
        //noinspection unchecked
        return userId != null ? (T) userId : defaultUserId;
    }

    public static Object getUserType() {

        return getContext().getUserType();
    }

    public static <T> T getUserType(T defaultUserType) {
        Object userType = getContext().getUserType();
        //noinspection unchecked
        return userType != null ? (T) userType : defaultUserType;
    }

    public static String getPlatform() {

        return getContext().getPlatform();
    }

    public static String getTenantId() {

        return getContext().getTenantId();
    }

    public static void putBaseData(Object userId, Object userType, String platform, String tenantId) {

        getContext().putBaseData(userId, userType, platform, tenantId);
    }

    public static void putBaseData(Object userId, String platform) {

        getContext().putBaseData(userId, null, platform, null);
    }

    // ====

    public static UserDetail getUserDetail() {

        return getContext().getUserDetail();
    }

    public static <T> T getUserDetail(Class<T> clazz) {
        //noinspection unchecked
        return (T) getContext().getUserDetail();
    }

    public static Collection<String> getUserGroups() {

        return getContext().getUserGroups(null);
    }

    public static Collection<String> getUserGroups(Object groupType) {

        return getContext().getUserGroups(groupType);
    }

    // ====


    public static String buildToken(Token token) {

        return getTokenManager().buildToken(token);
    }

    public static Token parseToken(String token) {

        return getTokenManager().parseToken(token);
    }

    public static int verifyToken(String token) {

        return getTokenManager().verifyToken(token);
    }

    public static void deleteToken(String token, Integer reason) {

        getTokenManager().deleteToken(token, reason);
    }

    public static Object refreshToken(String token) {

        return getTokenManager().refreshToken(token);
    }

    // ====

    public static UserDetail getUserDetail(Object userId, Object userType) {

        return getUserManager().getUserDetail(userId, userType);
    }

    public static Collection<String> getUserGroups(Object userId, Object userType, Object groupType) {

        return getUserManager().getUserGroups(userId, userType, groupType);
    }

    // ====

    public static boolean hasPermission(Object userId, Object userType, String permission) {

        return getAccessController().hasPermission(userId, userType, permission);
    }

    public static boolean hasRole(Object userId, Object userType, String role) {

        return ((RbacAccessController) getAccessController()).hasRole(userId, userType, role);
    }

    // ====

    public static AccessController getAccessController() {
        AccessController accessController = getContext().getAccessController();
        Assert.notNull(accessController, "The access controller is null. ");
        return accessController;
    }

    public static DataController getDataController() {
        DataController dataController = getContext().getDataController();
        Assert.notNull(dataController, "The data controller is null. ");
        return dataController;
    }

    public static TokenManager getTokenManager() {
        TokenManager tokenManager = getContext().getTokenManager();
        Assert.notNull(tokenManager, "The token manager is null. ");
        return tokenManager;
    }

    public static UserManager getUserManager() {
        UserManager userManager = getContext().getUserManager();
        Assert.notNull(userManager, "The user manager is null. ");
        return userManager;
    }

}
