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

package ccc.services.ejb3.local;

import static javax.ejb.TransactionAttributeType.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.activation.MimeType;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ccc.commons.DBC;
import ccc.commons.IO;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.services.AuditLog;
import ccc.services.DataManager;
import ccc.services.ResourceDao;
import ccc.services.UserManager;
import ccc.services.ejb3.support.BaseDao;
import ccc.services.ejb3.support.CoreData;
import ccc.services.ejb3.support.Dao;
import ccc.services.ejb3.support.FsCoreData;
import ccc.services.ejb3.support.QueryNames;


/**
 * EJB implementation of the {@link DataManager} interface.
 *
 * TODO: Confirm the interaction between stateful and stateless session beans
 * w.r.t. propagating transactions...
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=DataManager.NAME)
@TransactionAttribute(REQUIRED)
@Local(DataManager.class)
public class DataManagerEJB implements DataManager {

    @Resource(mappedName = "java:/ccc") private DataSource _datasource;
    @EJB(name=UserManager.NAME) private UserManager    _users;
    @PersistenceContext(unitName = "ccc-persistence")
    private EntityManager _em;

    private ResourceDao _dao;
    private CoreData _cd;

    /** Constructor. */
    @SuppressWarnings("unused") public DataManagerEJB() { super(); }

    /**
     * Constructor.
     *
     * @param cd The JDBC datasource used to manage data.
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public DataManagerEJB(final CoreData cd, final ResourceDao dao) {
        DBC.require().notNull(cd);
        DBC.require().notNull(dao);
        _cd = cd;
        _dao = dao;
    }


    /** {@inheritDoc} */
    @Override
    public void createFile(final File file,
                           final UUID parentId,
                           final InputStream dataStream) {
        final Data data = create(dataStream, file.size());
        file.data(data);
        _dao.create(parentId, file);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFile(final UUID fileId,
                           final String title,
                           final String description,
                           final MimeType mimeType,
                           final int size,
                           final InputStream dataStream) {

        final File f = _dao.findLocked(File.class, fileId);
        f.title(title);
        f.description(description);
        f.mimeType(mimeType);
        f.size(size);
        f.data(create(dataStream, size)); // TODO: Delete old data?
        _dao.update(f);
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
                    IO.copy(is, dataStream);
                }
            }
        );
    }


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _cd = new FsCoreData();
        final Dao bdao = new BaseDao(_em);
        final AuditLog audit = new AuditLogEJB(_em);
        _dao = new ResourceDaoImpl(_users, audit, bdao);
    }
}
