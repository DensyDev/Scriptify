package org.densy.scriptify.api.script.security;

import org.densy.scriptify.api.script.security.exclude.SecurityExclude;

import java.util.Set;

/**
 * Manages class access based on security constraints, ensuring only allowed classes can be used.
 */
public interface SecurityClassAccessor {

    /**
     * Retrieves the set of class names that are allowed to be accessed or used.
     *
     * @return A set of strings representing the names of allowed classes
     */
    Set<SecurityExclude> getExcludes();
}