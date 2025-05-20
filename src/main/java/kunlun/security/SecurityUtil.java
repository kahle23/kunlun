/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security;

import kunlun.bean.BeanHolder;
import kunlun.common.constant.Nil;
import kunlun.core.AccessController;
import kunlun.core.DataController;
import kunlun.util.IterUtil;

import java.util.Collection;
import java.util.Map;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.security.TokenManager.Token;
import static kunlun.security.UserManager.UserDetail;
import static kunlun.util.Assert.*;

/**
 * The security tools.
 * @author Kahle
 */
public class SecurityUtil {

    // region ======== base methods ========

    public static SecurityContext getContext() {
        Map<String, SecurityContext> beans = BeanHolder.getBeans(SecurityContext.class);
        notEmpty(beans, "Please set the security context first. ");
        boolean isOne = beans.size() == ONE;
        isTrue(isOne, "The security context is limited to one. ");
        SecurityContext context = IterUtil.getFirst(beans.values());
        return notNull(context, "The security context is null. ");
    }

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
    // endregion


    // region ======== user related methods ========

    public static UserDetail getUserDetail() {

        return getContext().getUserDetail();
    }

    public static <T> T getUserDetail(Class<T> clazz) {
        //noinspection unchecked
        return (T) getContext().getUserDetail();
    }

    public static Collection<String> getUserPermissions() {

        return getContext().getUserPermissions();
    }

    public static String getUserGroup() {

        return IterUtil.getFirst(getContext().getUserGroups(Nulls.OBJ));
    }

    public static Collection<String> getUserGroups() {

        return getContext().getUserGroups(Nulls.OBJ);
    }

    public static Collection<String> getUserGroups(Object groupType) {

        return getContext().getUserGroups(groupType);
    }
    // endregion


    // region ======== access controller ========

    public static boolean hasPermission(Object userId, Object userType, String permission) {

        return getAccessController().hasPermission(userId, userType, permission);
    }
    // endregion


    // region ======== token manager ========

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
    // endregion


    // region ======== user manager ========

    public static UserDetail getUserDetail(Object userId, Object userType) {

        return getUserManager().getUserDetail(userId, userType);
    }

    public static Collection<String> getUserPermissions(Object userId, Object userType) {

        return getUserManager().getUserPermissions(userId, userType);
    }

    public static Collection<String> getUserGroups(Object userId, Object userType, Object groupType) {

        return getUserManager().getUserGroups(userId, userType, groupType);
    }
    // endregion


    // region ======== core components ========

    public static AccessController getAccessController() {
        AccessController accessController = getContext().getAccessController();
        return notNull(accessController, "The access controller is null. ");
    }

    public static DataController getDataController() {
        DataController dataController = getContext().getDataController();
        return notNull(dataController, "The data controller is null. ");
    }

    public static TokenManager getTokenManager() {
        TokenManager tokenManager = getContext().getTokenManager();
        return notNull(tokenManager, "The token manager is null. ");
    }

    public static UserManager getUserManager() {
        UserManager userManager = getContext().getUserManager();
        return notNull(userManager, "The user manager is null. ");
    }
    // endregion

}
