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
package ccc.persistence;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import ccc.domain.CCCException;
import ccc.domain.Data;
import ccc.persistence.streams.CoreData;
import ccc.serialization.IO;
import ccc.types.DBC;


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
    public Data create(final InputStream dataStream, final int length) {
        final Data d = new Data();
        final File dir = mkdir(d);
        final File f = new File(dir, d.id().toString());

        try {
            final FileOutputStream fos = new FileOutputStream(f);

            try {
                IO.copy(dataStream, fos);
            } catch (final Exception e) {
                LOG.error("Error writing to file "+f.getAbsolutePath(), e);
                throw e;
            } finally {
                attemptClose(f, fos);
            }
            LOG.debug("Wrote data to file store: "+f.getAbsolutePath());

        } catch (final FileNotFoundException e) {
            LOG.error("Failed to open file "+f.getAbsolutePath(), e);
            throw new CCCException(e);

        } catch (final Exception e) {
            attemptDelete(f);
            throw new CCCException(e);
        }

        return d;
    }


    /** {@inheritDoc} */
    @Override
    public void retrieve(final Data data, final StreamAction action) {
        final File dir = dirFor(data);
        final File f = new File(dir, data.id().toString());

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
            throw new CCCException(e);

        } catch (final Exception e) {
            LOG.error("Error processing file "+f.getAbsolutePath(), e);
            throw new CCCException(e);
        }
    }


    private void attemptClose(final File f, final Closeable fileStream) {
        try {
            fileStream.close();
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
        final char c0 = data.id().toString().charAt(0);
        final char c1 = data.id().toString().charAt(1);
        final char c2 = data.id().toString().charAt(2);
        final String sep = File.separator;
        final File dir = new File(_root, c0+sep+c1+sep+c2);
        return dir;
    }
}
