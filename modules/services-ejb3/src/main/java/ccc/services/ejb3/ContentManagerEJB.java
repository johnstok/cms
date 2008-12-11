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

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.services.AuditLog;
import ccc.services.ContentManager;
import ccc.services.QueryManager;


/**
 * EJB3 implementation of {@link ContentManager}.
 *
 * @author Civic Computing Ltd
 */
@Stateless(name="ContentManager")
@TransactionAttribute(REQUIRED)
@Local(ContentManager.class)
public final class ContentManagerEJB
    implements
        ContentManager {

    @PersistenceContext(unitName = "ccc-persistence")
    private EntityManager _em;

    @EJB(name="QueryManager") private QueryManager _qm;
    @EJB(name="AuditLog") private AuditLog _audit;

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
                      final QueryManager queryManager,
                      final AuditLog auditLog) {
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


    /* ===================================================================
     * RETRIEVE
     * =================================================================*/

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final ResourcePath path) {
        final Folder contentRoot = _qm.findContentRoot();
        try {
            return contentRoot.navigateTo(path);
        } catch (final CCCException e) {
            return null;
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
    public Folder lookupRoot() {
        return _qm.findContentRoot();
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

        // FIXME Don't check version!!!
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
        // FIXME Don't check version!!!
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
        // FIXME Don't check version!!!
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDisplayTemplate(final UUID folderId,
                                      final Template template) {
        create(folderId, template);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Template> lookupTemplates() {
        return _qm.list("allTemplates", Template.class);
    }

    /** {@inheritDoc} */
    @Override
    public void update(final Template t) {
        // FIXME Don't check version!!!
        final Template current = _em.find(Template.class, t.id());
        current.title(t.title());
        current.description(t.description());
        current.definition(t.definition());
        current.body(t.body());
        _audit.recordUpdate(current);
    }
}
