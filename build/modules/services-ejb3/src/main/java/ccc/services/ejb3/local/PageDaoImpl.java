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

import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.services.PageDao;
import ccc.services.ResourceDao;


/**
 * DAO with methods specific to a page.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="PageDao")
@TransactionAttribute(REQUIRED)
@Local(PageDao.class)
public class PageDaoImpl implements PageDao {

    @EJB(name="ResourceDao") private ResourceDao _dao;


    /** Constructor. */
    @SuppressWarnings("unused") public PageDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public PageDaoImpl(final ResourceDao dao) {
        _dao = dao;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final UUID id,
                       final String newTitle,
                       final Set<Paragraph> newParagraphs) {

        final Page page = _dao.findLocked(Page.class, id);

        page.title(newTitle);
        page.deleteAllParagraphs();

        for (final Paragraph paragraph : newParagraphs) {
            page.addParagraph(paragraph);
        }
        _dao.update(page);
    }
}
