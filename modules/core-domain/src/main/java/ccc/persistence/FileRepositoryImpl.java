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
import java.io.OutputStream;
import java.util.List;

import javax.persistence.EntityManager;

import ccc.domain.Data;
import ccc.domain.EntityNotFoundException;
import ccc.domain.File;
import ccc.domain.Setting;
import ccc.entities.IData;
import ccc.persistence.streams.CopyAction;
import ccc.persistence.streams.CoreData;
import ccc.types.DBC;


/**
 * EJB implementation of the {@link FileRepository} interface.
 *
 * @author Civic Computing Ltd.
 */
public class FileRepositoryImpl implements FileRepository {

    private Repository _repository;
    private CoreData _cd;

    /**
     * Constructor.
     *
     * @param cd The JDBC datasource used to manage data.
     * @param repository The DAO used for CRUD operations, etc.
     */
    public FileRepositoryImpl(final CoreData cd, final Repository repository) {
        DBC.require().notNull(cd);
        DBC.require().notNull(repository);
        _cd = cd;
        _repository = repository;
    }


    /**
     * Constructor.
     *
     * @param cd The JDBC datasource used to manage data.
     * @param em The JPA entity manager for this repository.
     */
    public FileRepositoryImpl(final CoreData cd, final EntityManager em) {
        this(cd, new JpaRepository(em));
    }

    /**
     * Create a file repository that reads / writes to the file system.
     *
     * @param em The entity manager to use.
     *
     * @return The file repository.
     */
    public static FileRepository onFileSystem(final EntityManager em) {
        try {
            final SettingsRepository settings = new SettingsRepository(em);
            final Setting filestorePath =
                settings.find(Setting.Name.FILE_STORE_PATH);
            return new FileRepositoryImpl(
                new FsCoreData(filestorePath.value()),
                em);
        } catch (final EntityNotFoundException e) {
            throw new RuntimeException(
                "Setting missing: "+Setting.Name.FILE_STORE_PATH);
        }
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
            (Data) data, // TODO: Shouldn't need to cast here.
            new CopyAction(dataStream)
        );
    }
}
