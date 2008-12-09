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

import java.util.Set;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
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
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.domain.Setting;
import ccc.domain.Template;
import ccc.services.AuditLogLocal;
import ccc.services.ContentManagerLocal;
import ccc.services.ContentManagerRemote;
import ccc.services.QueryManagerLocal;


/**
 * EJB3 implementation of {@link ContentManager}.
 *
 * @author Civic Computing Ltd
 */
@Stateless(name="ContentManager")
@TransactionAttribute(REQUIRED)
@Remote(ContentManagerRemote.class)
@Local(ContentManagerLocal.class)
public final class ContentManagerEJB
    implements
        ContentManagerRemote,
        ContentManagerLocal {

    @PersistenceContext(unitName = "ccc-persistence")
    private EntityManager _em;

    @EJB(name="QueryManager", beanInterface=QueryManagerLocal.class)
    private QueryManagerLocal _qm;
    @EJB(name="AuditLog", beanInterface=AuditLogLocal.class)
    private AuditLogLocal _audit;

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
     * @param auditLog  An audit logger.
     */
    ContentManagerEJB(final EntityManager entityManager,
                      final QueryManagerLocal queryManager,
                      final AuditLogLocal auditLog) {
        _em = entityManager;
        _qm = queryManager;
        _audit = auditLog;
    }


    /* ===================================================================
     * CREATE
     * =================================================================*/

    private void create(final UUID folderId, final Resource newResource) {
        final Folder folder = lookup(folderId).as(Folder.class);
        if (null==folder) {
            throw new CCCException("No folder exists with id: "+folderId);
        }
        folder.add(newResource);
        _em.persist(newResource);
        _audit.recordCreate(newResource);
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
            _audit.recordCreate(root);
        }
    }


    /* ===================================================================
     * RETRIEVE
     * =================================================================*/

    /**
     * {@inheritDoc}
     */
    @Override
    public Maybe<Resource> lookup(final ResourcePath path) {
        final Folder contentRoot = _qm.findContentRoot().get();
        try {
            return
                new Maybe<Resource>(
                contentRoot.navigateTo(path));
        } catch (final CCCException e) {
            return new Maybe<Resource>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final UUID id) {
        return _em.find(Resource.class, id);
    }

    /**
     * {@inheritDoc}
     */
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
        if (p.template() != null) {
            p.template().body();
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
                       final Set<Paragraph> newParagraphs) {

        final Page page = lookup(id).as(Page.class);
        page.title(newTitle);
        page.deleteAllParagraphs();

        for (final Paragraph paragraph : newParagraphs) {
            page.addParagraph(paragraph);
        }
        _audit.recordUpdate(page);
    }


    /* ===================================================================
     * OTHER
     * =================================================================*/

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultTemplate(final Template newDefault) {
        final Folder rootFolder = lookupRoot();
        rootFolder.template(newDefault);
        _audit.recordChangeTemplate(rootFolder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTemplateForResource(final UUID resourceId,
                                          final Template template) {
        final Resource r = lookup(resourceId);
        r.template(template);
        _audit.recordChangeTemplate(r);
    }

    /** {@inheritDoc} */
    @Override
    public void create(final UUID folderId, final Alias alias) {
        create(folderId, (Resource) alias);
    }

    /** {@inheritDoc} */
    @Override
    public void move(final UUID resourceid, final UUID newParentId) {
        final Resource resource = lookup(resourceid);
        final Folder newParent =
            lookup(newParentId).as(Folder.class);
        final Folder oldParent =
            lookup(resource.parent().id()).as(Folder.class);

        oldParent.remove(resource);
        newParent.add(resource);
        _audit.recordMove(resource);
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final UUID targetId, final UUID aliasId) {
        final Resource target = lookup(targetId);
        final Alias alias = lookup(aliasId).as(Alias.class);

        alias.target(target);
        _audit.recordUpdate(alias);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final UUID resourceid, final String name) {
        final Resource resource = lookup(resourceid);
        resource.name(new ResourceName(name));
        _audit.recordRename(resource);
    }
}
