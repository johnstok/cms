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

import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.domain.Alias;
import ccc.domain.Resource;
import ccc.services.AliasDao;
import ccc.services.ResourceDao;


/**
 * DAO with methods specific to a template.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="AliasDao")
@TransactionAttribute(REQUIRED)
@Local(AliasDao.class)
public class AliasDaoImpl implements AliasDao {

    @EJB(name="ResourceDao") private ResourceDao _dao;


    /** Constructor. */
    @SuppressWarnings("unused") public AliasDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public AliasDaoImpl(final ResourceDao dao) {
        _dao = dao;
    }


    /** {@inheritDoc} */
    @Override
    public void updateAlias(final UUID targetId,
                            final UUID aliasId) {
        final Resource target = _dao.find(Resource.class, targetId);
        final Alias alias = _dao.findLocked(Alias.class, aliasId);

        alias.target(target);

        _dao.update(alias);
    }
}
