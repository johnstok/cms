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

package ccc.rendering;

import java.util.UUID;

import ccc.domain.EntityNotFoundException;
import ccc.domain.File;
import ccc.domain.Resource;
import ccc.entities.IResource;
import ccc.persistence.DataRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.streams.ReadToStringAction;
import ccc.types.ResourcePath;


/**
 * Implementation of {@link StatefulReader}.
 *
 * @author Civic Computing Ltd
 */
public final class StatefulReaderImpl
    implements
        StatefulReader {

    private final ResourceRepository  _resources;
    private final DataRepository _data;

    /**
     * Constructor.
     *
     * @param resources The DAO to use.
     * @param data The data manager to use.
     */
    public StatefulReaderImpl(final ResourceRepository resources,
                              final DataRepository data) {
        _resources = resources;
        _data = data;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public IResource resourceFromPath(final String absolutePath) {
        return continuityForPath(absolutePath).forCurrentRevision();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public IResource resourceFromId(final String id) {
        try {
            return _resources.find(
                Resource.class, UUID.fromString(id)).forCurrentRevision();
        } catch (final EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }


    /** {@inheritDoc} */
    @Override
    public String fileContentsFromPath(final String absolutePath,
                                       final String charset) {
        final StringBuilder sb = new StringBuilder();
        final Resource r = continuityForPath(absolutePath);
        if (r instanceof File) {
            final File f = (File) r;
            if (f.isText()) {
                _data.retrieve(
                    f.data(),
                    new ReadToStringAction(sb, charset)
                );
            }
        }
        return sb.toString();
    }


    private Resource continuityForPath(final String absolutePath) {
        final ResourcePath rp = new ResourcePath(absolutePath);
        try {
            return _resources.lookup(rp.top().toString(), rp.removeTop());
        } catch (final EntityNotFoundException e) {
            return null;
        }
    }
}
