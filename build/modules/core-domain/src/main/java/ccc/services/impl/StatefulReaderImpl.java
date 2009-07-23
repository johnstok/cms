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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.UUID;

import ccc.commons.IO;
import ccc.domain.File;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.DataManager;
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

    /**
     * A stream action that can read a raw bytes into a string.
     *
     * @author Civic Computing Ltd.
     */
    private static final class ReadContentToStringAction
        implements
            DataManager.StreamAction {

        private final StringBuilder _sb;
        private final String        _charset;

        /**
         * Constructor.
         *
         * @param sb The string builder to read into.
         * @param charset The character set to use.
         */
        private ReadContentToStringAction(final StringBuilder sb,
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


    private final ResourceDao  _resources;
    private final DataManager _data;

    /**
     * Constructor.
     *
     * @param resources The DAO to use.
     * @param data The data manager to use.
     */
    public StatefulReaderImpl(final ResourceDao resources,
                              final DataManager data) {
        _resources = resources;
        _data = data;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Resource resourceFromPath(final String absolutePath) {
        final ResourcePath rp = new ResourcePath(absolutePath);
        return _resources.lookup(rp.top().toString(), rp.removeTop());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Resource resourceFromId(final String id) {
        return _resources.find(Resource.class, UUID.fromString(id));
    }


    /** {@inheritDoc} */
    @Override
    public String fileContentsFromPath(final String absolutePath,
                                       final String charset) {
        final StringBuilder sb = new StringBuilder();
        final Resource r = resourceFromPath(absolutePath);
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
}
