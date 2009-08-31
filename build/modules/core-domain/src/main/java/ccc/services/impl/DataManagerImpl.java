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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ccc.domain.Data;
import ccc.domain.File;
import ccc.entities.IData;
import ccc.persistence.FileRepository;
import ccc.persistence.QueryNames;
import ccc.persistence.Repository;
import ccc.persistence.StreamAction;
import ccc.persistence.streams.CopyAction;
import ccc.persistence.streams.CoreData;
import ccc.types.DBC;


/**
 * EJB implementation of the {@link FileRepository} interface.
 *
 * @author Civic Computing Ltd.
 */
public class DataManagerImpl implements FileRepository {

    private Repository _repository;
    private CoreData _cd;

    /**
     * Constructor.
     *
     * @param cd The JDBC datasource used to manage data.
     * @param repository The DAO used for CRUD operations, etc.
     */
    public DataManagerImpl(final CoreData cd, final Repository repository) {
        DBC.require().notNull(cd);
        DBC.require().notNull(repository);
        _cd = cd;
        _repository = repository;
    }


    /** {@inheritDoc} */
    @Override
    public List<File> findImages() {
        return _repository.list(QueryNames.ALL_IMAGES, File.class);
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
    public void retrieve(final IData data, final OutputStream dataStream) {
        retrieve(
            (Data) data, // FIXME!!!!
            new CopyAction(dataStream)
        );
    }
}
