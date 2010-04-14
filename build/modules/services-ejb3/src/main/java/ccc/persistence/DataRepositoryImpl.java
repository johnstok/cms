/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import java.io.InputStream;

import javax.persistence.EntityManager;

import ccc.api.StreamAction;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.DBC;
import ccc.domain.Data;
import ccc.domain.Setting;
import ccc.persistence.streams.CoreData;


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
                new DataRepositoryImpl(new FsCoreData(filestorePath.getValue()));

        } catch (final EntityNotFoundException e) {
            throw new RuntimeException(
                "Setting missing: "+Setting.Name.FILE_STORE_PATH);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Data create(final InputStream dataStream, final long length) {
        return _cd.create(dataStream, length);
    }


    /** {@inheritDoc} */
    @Override
    public void retrieve(final Data data, final StreamAction action) {
        _cd.retrieve(data, action);
    }

}
