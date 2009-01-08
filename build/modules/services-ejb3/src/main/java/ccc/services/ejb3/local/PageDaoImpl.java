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

import java.util.Set;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;

import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.services.AuditLog;
import ccc.services.PageDao;
import ccc.services.UserManager;
import ccc.services.ejb3.support.BaseResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="PageDao")
@TransactionAttribute(REQUIRED)
@Local(PageDao.class)
public class PageDaoImpl extends BaseResourceDao implements PageDao {

    @EJB(name="UserManager") protected UserManager _users;

    /** Constructor. */
    @SuppressWarnings("unused") public PageDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param em
     * @param al
     */
    public PageDaoImpl(final EntityManager em,
                       final AuditLog al,
                       final UserManager um) {
        _audit = al;
        _em = em;
        _users = um;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final UUID folderId, final Page newPage) {
        create(folderId, (Resource) newPage);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final UUID id,
                       final long version,
                       final String newTitle,
                       final Set<Paragraph> newParagraphs) {

        final Page page = find(Page.class, id, version);
        page.confirmLock(_users.loggedInUser());

        page.title(newTitle);
        page.deleteAllParagraphs();

        for (final Paragraph paragraph : newParagraphs) {
            page.addParagraph(paragraph);
        }
        _audit.recordUpdate(page);
    }
}
