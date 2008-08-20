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

import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.persistence.PersistenceContextType.EXTENDED;

import java.util.List;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.services.AssetManager;


/**
 * EJB3 implementation of {@link AssetManager}.
 *
 * @author Civic Computing Ltd.
 */
@Stateful
@TransactionAttribute(REQUIRED)
@Remote(AssetManager.class)
@Local(AssetManager.class)
public class AssetManagerEJB implements AssetManager {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED)
    private EntityManager _entityManager;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private AssetManagerEJB() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     */
    AssetManagerEJB(final EntityManager entityManager) {
        _entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void createDisplayTemplate(final Template template) {
        _entityManager.persist(template);
        templatesFolder().add(template);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createRoot() {
        try {
            new Queries().lookupRoot(_entityManager,
                                     PredefinedResourceNames.ASSETS);
        } catch (final NoResultException e) {
            final Folder root = new Folder(PredefinedResourceNames.ASSETS);
            final Folder templates = new Folder(new ResourceName("templates"));
            _entityManager.persist(templates);
            _entityManager.persist(root);
            root.add(templates);
        }
    }

    /**
     * {@inheritDoc}
     * TODO: Remove - duplicate of same method on content manager.
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <T extends Resource> T lookup(final UUID id) {
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

        final Folder assetRoot =
            new Queries().lookupRoot(_entityManager,
                PredefinedResourceNames.ASSETS);
        final Folder templates =
            assetRoot
            .navigateTo(new ResourcePath("/templates/"));
        return templates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createFile(final File file, final String path) {
        _entityManager.persist(file.fileData());
        _entityManager.persist(file);
        Folder folder = (Folder) new Queries().lookupRoot(
            _entityManager,
            PredefinedResourceNames.CONTENT).navigateTo(new ResourcePath(path));
        folder.add(file);
    }

}
