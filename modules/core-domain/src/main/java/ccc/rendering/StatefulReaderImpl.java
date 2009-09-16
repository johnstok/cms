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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.UUID;

import ccc.domain.EntityNotFoundException;
import ccc.domain.File;
import ccc.domain.Resource;
import ccc.entities.IResource;
import ccc.persistence.FileRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.StreamAction;
import ccc.serialization.IO;
import ccc.types.ResourcePath;


/**
 * Implementation of {@link StatefulReader}.
 *
 * @author Civic Computing Ltd
 */
public final class StatefulReaderImpl
    implements
        StatefulReader {

    /**
     * A stream action that can read a raw bytes into a string.
     *
     * @author Civic Computing Ltd.
     */
    private static final class ReadContentToStringAction
        implements
            StreamAction {

        private final StringBuilder _sb;
        private final String        _charset;

        /**
         * Constructor.
         *
         * @param sb The string builder to read into.
         * @param charset The character set to use.
         */
        ReadContentToStringAction(final StringBuilder sb,
                                  final String charset) {
            _sb = sb;
            _charset = charset;
        }

        @Override public void execute(final InputStream is)
                                              throws Exception {
            final ByteArrayOutputStream os =
                new ByteArrayOutputStream();
            IO.copy(is, os);
            _sb.append(
                new String(
                    os.toByteArray(),
                    Charset.forName(_charset)));
        }
    }


    private final ResourceRepository  _resources;
    private final FileRepository _data;

    /**
     * Constructor.
     *
     * @param resources The DAO to use.
     * @param data The data manager to use.
     */
    public StatefulReaderImpl(final ResourceRepository resources,
                              final FileRepository data) {
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
                    new ReadContentToStringAction(sb, charset)
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
