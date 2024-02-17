/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.supplier;

import kunlun.core.Supplier;

import java.util.Collection;

/**
 * The user group data supplier.
 * @author Kahle
 */
public interface GroupDataSupplier extends Supplier {

    /**
     * Gets a list of the groups to which the user belongs.
     * @param userId The unique identification of the user
     * @param userType The user type (for example, toC user, toB user, or admin user)
     * @param groupType The user group type, such as department, region, etc
     * @return The groups list
     */
    Collection<String> getGroups(Object userId, Object userType, Object groupType);

}
