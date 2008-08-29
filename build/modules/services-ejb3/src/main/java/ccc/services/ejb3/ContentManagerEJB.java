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
import javax.persistence.PersistenceContext;

import ccc.commons.Maybe;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.domain.Template;
import ccc.services.ContentManager;
import ccc.services.QueryManager;


/**
 * EJB3 implementation of {@link ContentManager}.
 *
 * @author Civic Computing Ltd
 */
@Stateful
@TransactionAttribute(REQUIRED)
@Remote(ContentManager.class)
@Local(ContentManager.class)
public final class ContentManagerEJB implements ContentManager {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED)
    private EntityManager _em;

    @javax.annotation.Resource(mappedName="QueryManagerEJB/local")
    private QueryManager _qm;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private ContentManagerEJB() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     * @param queryManager A CCC QueryManager.
     */
    ContentManagerEJB(final EntityManager entityManager,
                             final QueryManager queryManager) {
        _em = entityManager;
        _qm = queryManager;
    }


    /* ===================================================================
     * CREATE
     * =================================================================*/

    private void create(final UUID folderId, final Resource newResource) {
        final Folder folder = lookup(folderId);
        if (null==folder) {
            throw new CCCException("No folder exists with id: "+folderId);
        }
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
        final Maybe<Folder> contentRoot =
            _qm.lookupRoot(PredefinedResourceNames.CONTENT);

        if (!contentRoot.isPresent()) {
            _em.persist(new Folder(PredefinedResourceNames.CONTENT));
        }
    }


    /* ===================================================================
     * RETRIEVE
     * =================================================================*/

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <T extends Resource> T lookup(final ResourcePath path) {
        return
            (T) _qm.lookupRoot(
                PredefinedResourceNames.CONTENT).get().navigateTo(path);
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
    @SuppressWarnings("unchecked")
    @Override
    public final Page eagerPageLookup(final ResourcePath path) {
        final Resource resource = _qm.lookupRoot(
            PredefinedResourceNames.CONTENT).get().navigateTo(path);
        if (resource == null) {
            return null;
        }
        if (resource.type() != ResourceType.PAGE) {
            throw new CCCException("Id does not belong to a page.");
        }
        final Page p = resource.as(Page.class);
        p.paragraphs().size();
        if (p.displayTemplateName() != null) {
            p.displayTemplateName().body();
        }
        return p;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Folder lookupRoot() {
        return _qm.lookupRoot(PredefinedResourceNames.CONTENT).get();
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


    /* ===================================================================
     * OTHER
     * =================================================================*/

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultTemplate(final Template newDefault) {
        lookupRoot().displayTemplateName(newDefault);
    }
}
