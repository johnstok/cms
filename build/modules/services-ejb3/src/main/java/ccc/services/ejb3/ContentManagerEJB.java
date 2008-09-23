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

import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.persistence.PersistenceContextType.EXTENDED;

import java.util.Map;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commons.Maybe;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.domain.Setting;
import ccc.domain.Template;
import ccc.services.ContentManagerLocal;
import ccc.services.ContentManagerRemote;
import ccc.services.QueryManagerLocal;


/**
 * EJB3 implementation of {@link ContentManager}.
 *
 * @author Civic Computing Ltd
 */
@Stateful(name="ContentManager")
@TransactionAttribute(REQUIRED)
@Remote(ContentManagerRemote.class)
@Local(ContentManagerLocal.class)
public final class ContentManagerEJB
    implements
        ContentManagerRemote,
        ContentManagerLocal {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED)
    private EntityManager _em;

    @EJB(name="QueryManager", beanInterface=QueryManagerLocal.class)
    private QueryManagerLocal _qm;

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
                      final QueryManagerLocal queryManager) {
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
    public Folder create(final UUID folderId, final Folder newFolder) {
        create(folderId, (Resource) newFolder);
        return newFolder;
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
    public void createRoot() {
        final Maybe<Folder> contentRoot =
            _qm.findContentRoot();

        if (!contentRoot.isDefined()) {
            final Folder root = new Folder(PredefinedResourceNames.CONTENT);
            _em.persist(root);
            _em.persist(
                new Setting(
                    Setting.Name.CONTENT_ROOT_FOLDER_ID,
                    root.id().toString()));
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
    public <T extends Resource> Maybe<T> lookup(final ResourcePath path) {
        final Folder contentRoot = _qm.findContentRoot().get();
        try {
            return
                new Maybe<T>(
                (T) contentRoot.navigateTo(path));
        } catch (final CCCException e) {
            return new Maybe<T>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> T lookup(final UUID id) {
        return (T) _em.find(Resource.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Page eagerPageLookup(final ResourcePath path) {
        final Resource resource = _qm.findContentRoot().get().navigateTo(path);
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
    public Folder lookupRoot() {
        return _qm.findContentRoot().get();
    }


    /* ===================================================================
     * UPDATE
     * =================================================================*/

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final UUID id,
                             final String newTitle,
                             final Map<String, String> newParagraphs) {

        final Page page = lookup(id);
        page.title(newTitle);
        page.deleteAllParagraphs();

        for (final Map.Entry<String, String> paragraph
                : newParagraphs.entrySet()) {
            page.addParagraph(
                paragraph.getKey(),
                new Paragraph(paragraph.getValue()));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTemplateForResource(final UUID resourceId,
                                          final Template template) {
        final Resource r = lookup(resourceId);
        r.displayTemplateName(template);
        _em.persist(r);
    }

    /**
     * @see ccc.services.ContentManager#create(java.util.UUID, ccc.domain.Alias)
     */
    @Override
    public void create(final UUID folderId, final Alias alias) {
        create(folderId, (Resource) alias);
    }
}
