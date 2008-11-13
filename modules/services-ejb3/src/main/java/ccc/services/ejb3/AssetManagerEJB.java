/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static javax.ejb.TransactionAttributeType.*;
import static javax.persistence.PersistenceContextType.*;

import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commons.Maybe;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.Setting;
import ccc.domain.Template;
import ccc.domain.Setting.Name;
import ccc.services.AssetManagerLocal;
import ccc.services.AssetManagerRemote;
import ccc.services.AuditLogLocal;
import ccc.services.QueryManagerLocal;


/**
 * EJB3 implementation of {@link AssetManager}.
 * TODO: Revisit our 'update' methods - because we do manual copy we lose the
 * benefit of optimistic offline lock.
 *
 * @author Civic Computing Ltd.
 */
@Stateful(name="AssetManager")
@TransactionAttribute(REQUIRED)
@Remote(AssetManagerRemote.class)
@Local(AssetManagerLocal.class)
public final class AssetManagerEJB
    implements
        AssetManagerLocal,
        AssetManagerRemote {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED)
    private EntityManager _entityManager;


    @EJB(name="QueryManager", beanInterface=QueryManagerLocal.class)
    private QueryManagerLocal _qm;
    @EJB(name="AuditLog", beanInterface=AuditLogLocal.class)
    private AuditLogLocal _audit;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private AssetManagerEJB() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     * @param queryManager A CCC query manager.
     * @param auditLog An audit logger.
     */
    AssetManagerEJB(final EntityManager entityManager,
                    final QueryManagerLocal queryManager,
                    final AuditLogLocal auditLog) {
        _entityManager = entityManager;
        _qm = queryManager;
        _audit = auditLog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final ResourcePath path) {
        return _qm.findAssetsRoot().get().navigateTo(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDisplayTemplate(final Template template) {
        _entityManager.persist(template);
        templatesFolder().add(template);
        _audit.recordCreate(template);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createRoot() {
        final Maybe<Folder> assetRoot = _qm.findAssetsRoot();

        if (!assetRoot.isDefined()) {
            final Folder root = new Folder(PredefinedResourceNames.ASSETS);
            final Folder templates = new Folder(new ResourceName("templates"));
            _entityManager.persist(templates);
            _entityManager.persist(root);
            _entityManager.persist(
                new Setting(Name.ASSETS_ROOT_FOLDER_ID, root.id().toString()));
            root.add(templates);

            _audit.recordCreate(root);
            _audit.recordCreate(templates);
        }
    }

    /**
     * {@inheritDoc}
     * TODO: Remove - duplicate of same method on content manager.
     */
    @Override
    public Resource lookup(final UUID id) {
        return _entityManager.find(Resource.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Template> lookupTemplates() {
        return templatesFolder().entries(Template.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Template createOrRetrieve(final Template template) {

        final Folder templatesFolder = templatesFolder();

        for (final Template t : templatesFolder.entries(Template.class)) {
            if (template.equals(t)) {
                return t;
            }
        }

        _entityManager.persist(template);
        templatesFolder.add(template);
        _audit.recordCreate(template);
        return template;
    }

    private Folder templatesFolder() {
        final Folder assetRoot = _qm.findAssetsRoot().get();
        final Folder templates =
            assetRoot
                .navigateTo(new ResourcePath("/templates"))
                .as(Folder.class);
        return templates;
    }

    /** {@inheritDoc} */
    @Override
    public void update(final Template t) {
        final Template current = _entityManager.find(Template.class, t.id());
        current.title(t.title());
        current.description(t.description());
        current.definition(t.definition());
        current.body(t.body());
        _audit.recordUpdate(current);
    }

}
