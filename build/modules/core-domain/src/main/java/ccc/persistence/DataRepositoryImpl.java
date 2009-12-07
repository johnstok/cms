/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import java.io.InputStream;

import javax.persistence.EntityManager;

import ccc.domain.Data;
import ccc.domain.EntityNotFoundException;
import ccc.domain.Setting;
import ccc.persistence.streams.CoreData;
import ccc.types.DBC;


/**
 * EJB implementation of the {@link DataRepository} interface.
 *
 * @author Civic Computing Ltd.
 */
class DataRepositoryImpl implements DataRepository {

    private CoreData _cd;

    /**
     * Constructor.
     *
     * @param cd The JDBC datasource used to manage data.
     */
    public DataRepositoryImpl(final CoreData cd) {
        DBC.require().notNull(cd);
        _cd = cd;
    }


    /**
     * Create a file repository that reads / writes to the file system.
     *
     * @param em The entity manager to use.
     *
     * @return The file repository.
     */
    public static DataRepository onFileSystem(final EntityManager em) {
        try {
            final SettingsRepository settings = new SettingsRepository(em);
            final Setting filestorePath =
                settings.find(Setting.Name.FILE_STORE_PATH);
            return
                new DataRepositoryImpl(new FsCoreData(filestorePath.value()));

        } catch (final EntityNotFoundException e) {
            throw new RuntimeException(
                "Setting missing: "+Setting.Name.FILE_STORE_PATH);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Data create(final InputStream dataStream, final int length) {
        return _cd.create(dataStream, length);
    }


    /** {@inheritDoc} */
    @Override
    public void retrieve(final Data data, final StreamAction action) {
        _cd.retrieve(data, action);
    }

}
