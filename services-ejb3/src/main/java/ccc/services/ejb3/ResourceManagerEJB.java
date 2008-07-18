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

import static ccc.domain.Queries.*;
import static javax.ejb.TransactionAttributeType.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
@Stateless
@TransactionAttribute(REQUIRED)
public class ResourceManagerEJB implements ResourceManager {

    @PersistenceContext(unitName = "ccc-persistence")
    private final EntityManager em;

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     */
    public ResourceManagerEJB(final EntityManager entityManager) {

        em = entityManager;
    }

    /**
     * @see ResourceManager#lookup(java.lang.String)
     */
    @Override
    public Resource lookup(final ResourcePath path) {
        return contentRoot().navigateTo(path);
    }

    /**
     * @see ccc.services.ResourceManager#createFolder(java.lang.String)
     */
    @Override
    public void createFolder(final String pathString) {
        ResourcePath path = new ResourcePath(pathString);
        Folder currentFolder = contentRoot();
        for (ResourceName name : path.elements()) {
            try {
                currentFolder = currentFolder.findEntryByName(name).asFolder();
            } catch(CCCException e) {
                Folder newFolder = new Folder(name);
                currentFolder.add(newFolder);
                currentFolder = newFolder;
            }
        }
    }

    /**
     * @see ccc.services.ResourceManager#createRoot()
     */
    @Override
    public void createRoot() {
        Folder contentRoot = contentRoot();
        if (null == contentRoot) {
            em.persist(new Folder(PredefinedResourceNames.CONTENT));
        }
    }

    /**
     * Look up the root folder for the content hierarchy.
     * TODO: Factor this method to a helper class?
     *
     * @return
     */
    private Folder contentRoot() {

        Query q = em.createNamedQuery(RESOURCE_BY_URL);
        q.setParameter("url", PredefinedResourceNames.CONTENT);
        Object singleResult = q.getSingleResult();

        Folder contentRoot = Folder.class.cast(singleResult);
        return contentRoot;
    }
}
