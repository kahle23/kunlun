/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support;

import kunlun.core.AccessController;
import kunlun.security.UserService;
import kunlun.util.StrUtil;

import java.util.Collection;

/**
 * The simple access controller.
 * @author Kahle
 */
public class SimpleAccessController implements AccessController {
    private final UserService userService;

    public SimpleAccessController(UserService userService) {

        this.userService = userService;
    }

    public SimpleAccessController() {

        this(null);
    }

    public UserService getUserService() {

        return userService;
    }

    @Override
    public boolean hasPermission(Object userId, Object userType, String permission) {
        // Is not turn on access permission or permission code is blank.
        if (getUserService() == null) { return true; }
        if (StrUtil.isBlank(permission)) { return true; }
        // Get user permissions.
        Collection<String> permissions = getUserService().getPermissions(userId, userType);
        // Is not turn on access permission.
        if (permissions == null) { return true; }
        // Judge access permission.
        return permissions.contains(permission);
    }

}
