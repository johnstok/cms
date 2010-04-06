/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.scripting.rhino;

import java.util.List;

import org.apache.log4j.Logger;
import org.mozilla.javascript.ClassShutter;

import ccc.types.DBC;

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
        DBC.require().notNull(allowedClasses);
        _allowedClasses = allowedClasses;
        if (LOG.isDebugEnabled()) {
            for (final String allowed : _allowedClasses) {
                LOG.debug("Whitelisted: "+allowed);
            }
        }
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
