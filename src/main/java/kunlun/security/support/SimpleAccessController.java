/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support;

import kunlun.core.AccessController;
import kunlun.security.UserManager;
import kunlun.util.StringUtils;

import java.util.Collection;

/**
 * The simple access controller.
 * @author Kahle
 */
public class SimpleAccessController implements AccessController {
    private final UserManager userManager;

    public SimpleAccessController(UserManager userManager) {

        this.userManager = userManager;
    }

    public SimpleAccessController() {

        this(null);
    }

    public UserManager getUserManager() {

        return userManager;
    }

    @Override
    public boolean hasPermission(Object userId, Object userType, String permission) {
        // Is not turn on access permission or permission code is blank.
        if (getUserManager() == null) { return true; }
        if (StringUtils.isBlank(permission)) { return true; }
        // Get user permissions.
        Collection<String> permissions = getUserManager().getUserPermissions(userId, userType);
        // Is not turn on access permission.
        if (permissions == null) { return true; }
        // Judge access permission.
        return permissions.contains(permission);
    }

}
