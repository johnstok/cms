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
package ccc.persistence.jpa;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import ccc.commons.CCCProperties;
import ccc.commons.IO;
import ccc.domain.CCCException;
import ccc.domain.Data;
import ccc.services.CoreData;
import ccc.services.DataManager.StreamAction;


/**
 * An implementation of {@link CoreData} that reads from / writes to the file
 * system.
 *
 * @author Civic Computing Ltd.
 */
public class FsCoreData
    implements
        CoreData {
    private static final Logger LOG = Logger.getLogger(FsCoreData.class);

    private final File _root;

    /**
     * Constructor.
     */
    public FsCoreData() {
        _root = new File(CCCProperties.get("filestore.path"));
    }

    /** {@inheritDoc} */
    @Override
    public Data create(final InputStream dataStream, final int length) {
        final Data d = new Data();
        final File f = new File(_root, d.id().toString());

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
        final File f = new File(_root, data.id().toString());

        try {
            final FileInputStream fis = new FileInputStream(f);

            try {
                action.execute(fis);
            } finally {
                attemptClose(f, fis);
            }

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
        boolean deleted = f.delete();
        if (!deleted) {
            LOG.error("Failed to delete file "+f.getAbsolutePath());
        }
    }
}
