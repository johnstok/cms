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

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commons.Maybe;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.Setting;
import ccc.domain.Template;
import ccc.domain.Setting.Name;
import ccc.services.AssetManager;
import ccc.services.QueryManager;


/**
 * EJB3 implementation of {@link AssetManager}.
 *
 * @author Civic Computing Ltd.
 */
@Stateful
@TransactionAttribute(REQUIRED)
@Remote(AssetManager.class)
@Local(AssetManager.class)
public final class AssetManagerEJB implements AssetManager {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED)
    private EntityManager _entityManager;


    @javax.annotation.Resource(mappedName="QueryManagerEJB/local")
    private QueryManager _qm;

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
     */
    AssetManagerEJB(final EntityManager entityManager,
                    final QueryManager queryManager) {
        _entityManager = entityManager;
        _qm = queryManager;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> T lookup(final ResourcePath path) {
        return (T) _qm.findAssetsRoot().get().navigateTo(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDisplayTemplate(final Template template) {
        _entityManager.persist(template);
        templatesFolder().add(template);
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
        }
    }

    /**
     * {@inheritDoc}
     * TODO: Remove - duplicate of same method on content manager.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> T lookup(final UUID id) {
        return (T) _entityManager.find(Resource.class, id);
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

        return template;
    }

    private Folder templatesFolder() {
        final Folder assetRoot = _qm.findAssetsRoot().get();
        final Folder templates =
            assetRoot .navigateTo(new ResourcePath("/templates/"));
        return templates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createFile(final File file, final UUID parentId) {
        _entityManager.persist(file);
        final Folder folder = lookup(parentId).as(Folder.class);
        folder.add(file);
    }

}
