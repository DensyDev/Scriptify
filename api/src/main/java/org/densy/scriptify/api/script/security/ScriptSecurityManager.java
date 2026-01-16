package org.densy.scriptify.api.script.security;

import org.densy.scriptify.api.script.constant.ScriptConstantManager;
import org.densy.scriptify.api.script.security.exclude.SecurityExclude;

import java.util.Set;

public interface ScriptSecurityManager {

    /**
     * Gets a current security mode.
     *
     * @return The boolean associated with security mode state
     */
    boolean getSecurityMode();

    /**
     * Sets the current security mode.
     *
     * @param securityMode The boolean associated with security mode state
     * @see ScriptConstantManager
     */
    void setSecurityMode(boolean securityMode);

    /**
     * Receives security file system.
     *
     * @return Security file system
     */
    SecurityFileSystem getFileSystem();

    /**
     * Receives security path accessor.
     *
     * @return Security path accessor
     */
    SecurityPathAccessor getPathAccessor();

    /**
     * Retrieves all existing exclusions for this script.
     *
     * @return Set with exclusions
     */
    Set<SecurityExclude> getExcludes();

    /**
     * Adds an exclusion for a path or package.
     *
     * @param exclude The exclusion to be added
     */
    void addExclude(SecurityExclude exclude);

    /**
     * Removes an exclusion for a path or package.
     *
     * @param exclude The exclusion to remove
     */
    void removeExclude(SecurityExclude exclude);
}
