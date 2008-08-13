/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.services.ejb3;

import static javax.ejb.TransactionAttributeType.*;
import static javax.persistence.PersistenceContextType.*;

import java.util.Map;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ContentManager;


/**
 * EJB3 implementation of {@link ContentManager}.
 *
 * @author Civic Computing Ltd
 */
@Stateful
@TransactionAttribute(REQUIRED)
@Remote(ContentManager.class)
@Local(ContentManager.class)
public class ContentManagerEJB implements ContentManager {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED)
    private EntityManager _em;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private ContentManagerEJB() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     */
    public ContentManagerEJB(final EntityManager entityManager) {
        _em = entityManager;
    }


    /* ===================================================================
     * CREATE
     * =================================================================*/

    private void create(final UUID folderId, final Resource newResource) {
        final Folder folder = lookup(folderId);
        folder.add(newResource);
        _em.persist(newResource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void create(final UUID folderId, final Folder newFolder) {
        create(folderId, (Resource) newFolder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void create(final UUID folderId, final Page newPage) {
        create(folderId, (Resource) newPage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void createRoot() {
        try {
            new Queries().lookupRoot(_em, PredefinedResourceNames.CONTENT);
        } catch (final NoResultException e) {
            _em.persist(new Folder(PredefinedResourceNames.CONTENT));
        }
    }


    /* ===================================================================
     * RETRIEVE
     * =================================================================*/

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends Resource> T lookup(final ResourcePath path) {
        return
            new Queries().lookupRoot(
                _em, PredefinedResourceNames.CONTENT).navigateTo(path);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <T extends Resource> T lookup(final UUID id) {
        return (T) _em.find(Resource.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Folder lookupRoot() {
        return new Queries().lookupRoot(_em, PredefinedResourceNames.CONTENT);
    }


    /* ===================================================================
     * UPDATE
     * =================================================================*/

    /**
     * {@inheritDoc}
     */
    @Override
    public final void update(final UUID id,
                             final String newTitle,
                             final Map<String, String> newParagraphs) {

        final Page page = lookup(id);
        page.title(newTitle);
        page.deleteAllParagraphs();

        for (final String key : newParagraphs.keySet()) {
            page.addParagraph(key, new Paragraph(newParagraphs.get(key)));
        }
    }
}
