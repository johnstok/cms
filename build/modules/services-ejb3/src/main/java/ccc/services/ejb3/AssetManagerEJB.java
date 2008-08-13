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

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
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
        final Folder templateFolder =
            new Queries().lookupRoot(_entityManager, PredefinedResourceNames.ASSETS)
                .navigateTo(new ResourcePath("/templates/"));

        _entityManager.persist(template);
        templateFolder.add(template);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createRoot() {
        try {
            new Queries().lookupRoot(_entityManager, PredefinedResourceNames.ASSETS);
        } catch (final NoResultException e) {
            final Folder root = new Folder(PredefinedResourceNames.ASSETS);
            final Folder templates = new Folder(new ResourceName("templates"));
            _entityManager.persist(templates);
            _entityManager.persist(root);
            root.add(templates);
        }
    }

}
