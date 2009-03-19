/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.content.server;

import java.util.UUID;

import ccc.commons.DBC;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.User;
import ccc.services.StatefulReader;


/**
 * Default implementation of the {@link Locator} interface.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultLocator
    implements
        Locator {

    private final StatefulReader _reader;
    private final String _rootName;

    /**
     * Constructor.
     *
     * @param reader The reader to use for looking up resources.
     * @param rootName The name of the root folder to look up resources from.
     */
    public DefaultLocator(final StatefulReader reader,
                           final String rootName) {
        DBC.require().notNull(reader);
        DBC.require().notNull(rootName);

        _reader = reader;
        _rootName = rootName;
    }


    /** {@inheritDoc} */
    @Override
    public Resource locate(final ResourcePath resourcePath) {
        return applySecurity(_reader.lookup(_rootName, resourcePath));
    }


    /** {@inheritDoc} */
    @Override
    public Resource locate(final UUID resourceId) {
        throw new NotFoundException();
    }


    private Resource applySecurity(final Resource r) {
        if (null==r) {
            return null;
        }
        User u = _reader.loggedInUser();
        if (null==u) {
            u = new User("anonymous");
        }
        if (r.isAccessibleTo(u)) {
            return r;
        }
        throw new AuthenticationRequiredException(r);
    }
}
