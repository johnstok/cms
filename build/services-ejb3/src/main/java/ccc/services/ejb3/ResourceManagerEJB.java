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

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.domain.CCCException;
import ccc.domain.Content;
import ccc.domain.Folder;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.services.ResourceManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
@Stateless
@TransactionAttribute(REQUIRED)
@Remote(ResourceManager.class)
@Local(ResourceManager.class)
public class ResourceManagerEJB implements ResourceManager {

    @PersistenceContext(unitName = "ccc-persistence")
    private EntityManager em;

    /**
     * Constructor.
     */
    private ResourceManagerEJB() {}

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
        final ResourcePath path = new ResourcePath(pathString);
        createFoldersForPath(path.elements());
    }

    /**
     * Creates new folders for the path in case folders do not exist
     * already.
     *
     * @param elements
     * @return 
     */
    private Folder createFoldersForPath(final List<ResourceName> elements) {

        Folder currentFolder = contentRoot();
        for (final ResourceName name : elements) {
            try {
                currentFolder = currentFolder.findEntryByName(name).asFolder();
            } catch(final CCCException e) {
                final Folder newFolder = new Folder(name);
                em.persist(newFolder);
                currentFolder.add(newFolder);
                currentFolder = newFolder;
            } catch(ClassCastException e) {
                System.err.println("Retrived resource does not match expected type.");
            }
        }
        return currentFolder;
    }

    /**
     * @see ccc.services.ResourceManager#createRoot()
     */
    @Override
    public void createRoot() {
        try {
            contentRoot();
        } catch (final NoResultException e) {
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

        final Query q = em.createNamedQuery(RESOURCE_BY_URL);
        q.setParameter("name", PredefinedResourceNames.CONTENT);
        final Object singleResult = q.getSingleResult();

        final Folder contentRoot = Folder.class.cast(singleResult);
        return contentRoot;
    }

    /**
     * @see ccc.services.ResourceManager#createContent(java.lang.String)
     */
    @Override
    public void createContent(String pathString) {
        final ResourcePath path = new ResourcePath(pathString);
        Folder parentFolder = createFoldersForPath(path.elementsToTop());
        
        List<ResourceName> elements = path.elements();
        final ResourceName name = elements.get(elements.size()-1);
        
        boolean resourceExists = true;
        boolean resourceIsFolder = false;
        
        try {
            Resource resource = parentFolder.findEntryByName(name);
            resourceIsFolder = resource.type()==ResourceType.FOLDER;
        } catch(final CCCException e) {
            resourceExists = false;
        }
        
        if (resourceExists && resourceIsFolder) {
            throw new CCCException("A folder already exists at the path "+pathString);
        } else if (!resourceExists) {
            final Content newContent = new Content(name);
            em.persist(newContent);
            parentFolder.add(newContent);
        }
    }

    /**
     * @see ccc.services.ResourceManager#createParagraphsForContent(ccc.domain.Content, java.util.List)
     */
    @Override
    public void createParagraphsForContent(Content content, Map<String, Paragraph> paragraphs) {

        for (String key : paragraphs.keySet()) {
            Paragraph paragraph = paragraphs.get(key);
            em.persist(paragraph);
            content.addParagraph(key, paragraph);
        }
    }
}
