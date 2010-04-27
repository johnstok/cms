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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.persistence;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import ccc.api.core.StreamAction;
import ccc.api.types.DBC;
import ccc.commons.IO;
import ccc.domain.Data;
import ccc.persistence.streams.CoreData;


/**
 * An implementation of {@link CoreData} that reads from / writes to the file
 * system.
 *
 * @author Civic Computing Ltd.
 */
class FsCoreData
    implements
        CoreData {
    private static final Logger LOG = Logger.getLogger(FsCoreData.class);

    private final File _root;

    /**
     * Constructor.
     *
     * @param filestorePath The absolute path to the folder where files will be
     *  stored.
     */
    public FsCoreData(final String filestorePath) {
        DBC.require().notEmpty(filestorePath);
        final File f = new File(filestorePath);
        if (!f.exists()) {
            throw new IllegalArgumentException(
                "Path does not exist: "+f.getAbsolutePath());
        }
        if (!f.isDirectory()) {
            throw new IllegalArgumentException(
                "Path is not a directory: "+f.getAbsolutePath());
        }
        if (!f.canRead()) {
            throw new IllegalArgumentException(
                "Path is not readable: "+f.getAbsolutePath());
        }
        if (!f.canWrite()) {
            throw new IllegalArgumentException(
                "Path is not writeable: "+f.getAbsolutePath());
        }
        _root = f;
        LOG.debug("Created file store: "+_root.getAbsolutePath());
    }

    /** {@inheritDoc} */
    @Override
    public Data create(final InputStream dataStream, final long length) {
        final Data d = new Data();
        final File dir = mkdir(d);
        final File f = new File(dir, d.getId().toString());

        try {
            final FileOutputStream fos = new FileOutputStream(f);

            try {
                IO.copy(dataStream, fos);
            } catch (final Exception e) {
                LOG.error("Error writing to file "+f.getAbsolutePath(), e);
                throw e;
            } finally {
                attemptClose(f, fos);
                attemptClose(f, dataStream);
            }
            LOG.debug("Wrote data to file store: "+f.getAbsolutePath());

        } catch (final FileNotFoundException e) {
            LOG.error("Failed to open file "+f.getAbsolutePath(), e);
            throw new RuntimeException(e);

        } catch (final Exception e) {
            attemptDelete(f);
            throw new RuntimeException(e);
        }

        return d;
    }


    /** {@inheritDoc} */
    @Override
    public void retrieve(final Data data, final StreamAction action) {
        DBC.require().notNull(data);
        DBC.require().notNull(action);

        final File dir = dirFor(data);
        final File f = new File(dir, data.getId().toString());

        try {
            final FileInputStream fis = new FileInputStream(f);

            try {
                action.execute(fis);
            } finally {
                attemptClose(f, fis);
            }
            LOG.debug("Retrieved data from file store: "+f.getAbsolutePath());

        } catch (final FileNotFoundException e) {
            LOG.error("Failed to open file "+f.getAbsolutePath(), e);
            throw new RuntimeException(e);

        } catch (final Exception e) {
            LOG.error("Error processing file "+f.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }


    private void attemptClose(final File f, final Closeable closeable) {
        try {
            closeable.close();
        } catch (final IOException e) {
            LOG.error(
                "Failed to close stream on "+f.getAbsolutePath(), e);
        }
    }


    private void attemptDelete(final File f) {
        final boolean deleted = f.delete();
        if (!deleted) {
            LOG.error("Failed to delete file "+f.getAbsolutePath());
        }
    }


    private File mkdir(final Data data) {
        final File dir = dirFor(data);
        if (dir.exists()) { return dir; }
        if (!dir.mkdirs()) {
            throw new RuntimeException(
                "Unable to create directory: "+dir.getAbsolutePath());
        }
        return dir;
    }


    private File dirFor(final Data data) {
        final char c0 = data.getId().toString().charAt(0);
        final char c1 = data.getId().toString().charAt(1);
        final char c2 = data.getId().toString().charAt(2);
        final String sep = File.separator;
        final File dir = new File(_root, c0+sep+c1+sep+c2);
        return dir;
    }
}
