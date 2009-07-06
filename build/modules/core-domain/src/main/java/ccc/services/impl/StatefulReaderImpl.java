/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.services.impl;

import java.util.UUID;

import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ResourceDao;
import ccc.services.StatefulReader;


/**
 * Implementation of {@link StatefulReader}.
 *
 * @author Civic Computing Ltd
 */
public final class StatefulReaderImpl
    implements
        StatefulReader {

    private final ResourceDao  _resources;

    /**
     * Constructor.
     *
     * @param resources The DAO to use.
     */
    public StatefulReaderImpl(final ResourceDao resources) {
        _resources = resources;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final String rootName, final ResourcePath path) {
        return _resources.lookup(rootName, path);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final String id) {
        return _resources.find(Resource.class, UUID.fromString(id));
    }

    /** {@inheritDoc} */
    @Override
    public String absolutePath(final String legacyId) {
        final Resource r = _resources.lookupWithLegacyId(legacyId);
        return (null==r) ? null : r.absolutePath().toString();
    }
}
