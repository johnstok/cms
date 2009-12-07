/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import java.util.List;

import org.apache.log4j.Logger;
import org.mozilla.javascript.ClassShutter;

/**
 * Class shutter for the Rhino scripting environment.
 * <p>This classes uses a white-list approach to allow access to Java
 * classes within the Rhino Javascript environment.
 *
 * @author Civic Computing Ltd.
 */
final class CccClassShutter
    implements
        ClassShutter {

    private static final Logger LOG =
        Logger.getLogger(CccClassShutter.class);

    private final List<String> _allowedClasses;


    /**
     * Constructor.
     *
     * @param allowedClasses The classes to allow access to.
     */
    public CccClassShutter(final List<String> allowedClasses) {
        _allowedClasses = allowedClasses;
    }


    /** {@inheritDoc} */
    @Override
    public boolean visibleToScripts(final String fullClassName) {
        if (!isClass(fullClassName)) {
            LOG.debug("Allowed access to "+fullClassName);
            return true;
        } else if (isOnWhitelist(fullClassName)) {
            LOG.debug("Allowed access to "+fullClassName);
            return true;
        }
        LOG.debug("Disallowed access to "+fullClassName);
        throw new RuntimeException(
            "Access to class disallowed: "+fullClassName);
    }


    private boolean isOnWhitelist(final String fullClassName) {
        if (null==fullClassName) { return false; }
        for (final String allowedClass : _allowedClasses) {
            if (fullClassName.startsWith(allowedClass)) {
                return true;
            }
        }
        return false;
    }


    private boolean isClass(final String fullClassName) {
        try {
            Class.forName(fullClassName);
        } catch (final ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
