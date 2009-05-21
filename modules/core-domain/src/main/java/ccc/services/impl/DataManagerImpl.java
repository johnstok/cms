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

package ccc.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ccc.api.DBC;
import ccc.commons.IO;
import ccc.domain.CCCException;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.services.CoreData;
import ccc.services.Dao;
import ccc.services.DataManager;
import ccc.services.QueryNames;


/**
 * EJB implementation of the {@link DataManager} interface.
 *
 * @author Civic Computing Ltd.
 */
public class DataManagerImpl implements DataManager {

    private Dao _dao;
    private CoreData _cd;

    /**
     * Constructor.
     *
     * @param cd The JDBC datasource used to manage data.
     * @param dao The DAO used for CRUD operations, etc.
     */
    public DataManagerImpl(final CoreData cd, final Dao dao) {
        DBC.require().notNull(cd);
        DBC.require().notNull(dao);
        _cd = cd;
        _dao = dao;
    }


    /** {@inheritDoc} */
    @Override
    public List<File> findImages() {
        return _dao.list(QueryNames.ALL_IMAGES, File.class);
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


    /** {@inheritDoc} */
    @Override
    public void retrieve(final Data data, final OutputStream dataStream) {
        retrieve(
            data,
            new StreamAction(){
                @Override public void execute(final InputStream is) {
                    try {
                        IO.copy(is, dataStream);
                    } catch (final IOException e) {
                        // FIXME: choose a better exception.
                        throw new CCCException(e);
                    }
                }
            }
        );
    }
}
