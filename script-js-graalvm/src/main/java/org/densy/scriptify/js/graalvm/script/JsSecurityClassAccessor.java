package org.densy.scriptify.js.graalvm.script;

import org.densy.scriptify.api.script.security.SecurityClassAccessor;
import org.densy.scriptify.api.script.security.exclude.ClassSecurityExclude;
import org.densy.scriptify.api.script.security.exclude.PackageSecurityExclude;
import org.densy.scriptify.api.script.security.exclude.SecurityExclude;
import org.graalvm.polyglot.PolyglotException;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class JsSecurityClassAccessor implements Predicate<String>, SecurityClassAccessor {

    private final Set<SecurityExclude> excludes;
    private final Set<String> allowedClasses;
    private final Set<String> allowedPackages;

    public JsSecurityClassAccessor(Set<SecurityExclude> excludes) {
        this.excludes = excludes;
        this.allowedClasses = new HashSet<>();
        this.allowedPackages = new HashSet<>();

        for (SecurityExclude exclude : excludes) {
            if (exclude instanceof ClassSecurityExclude classExclude) {
                allowedClasses.add(classExclude.getValue());
            } else if (exclude instanceof PackageSecurityExclude packageExclude) {
                allowedPackages.add(packageExclude.getValue());
            }
        }

        this.allowedClasses.add(PolyglotException.class.getName());
    }

    @Override
    public Set<SecurityExclude> getExcludes() {
        return excludes;
    }

    @Override
    public boolean test(String className) {
        if (this.allowedClasses.contains(className)) {
            return true;
        }
        for (String exclude : this.allowedPackages) {
            if (className.startsWith(exclude)) {
                return true;
            }
        }
        return false;
    }
}
