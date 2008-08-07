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
import static javax.persistence.PersistenceContextType.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.services.ContentManager;


/**
 * EJB3 implementation of {@link ContentManager}.
 *
 * @author Civic Computing Ltd
 */
@Stateful
@TransactionAttribute(REQUIRED)
@Remote(ContentManager.class)
@Local(ContentManager.class)
public class ContentManagerEJB implements ContentManager {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED)
    private EntityManager _em;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private ContentManagerEJB() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     */
    public ContentManagerEJB(final EntityManager entityManager) {
        _em = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Resource lookup(final ResourcePath path) {
        return contentRoot().navigateTo(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void createFolder(final String pathString) {
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
                _em.persist(newFolder);
                currentFolder.add(newFolder);
                currentFolder = newFolder;
            } catch(final ClassCastException e) {
                System.err.println(
                    "Retrived resource does not match expected type.");
            }
        }
        return currentFolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void createRoot() {
        try {
            contentRoot();
        } catch (final NoResultException e) {
            _em.persist(new Folder(PredefinedResourceNames.CONTENT));
        }
    }

    /**
     * Look up the root folder for the content hierarchy.
     * TODO: Factor this method to a helper class?
     *
     * @return
     */
    private Folder contentRoot() {

        final Query q = _em.createNamedQuery(RESOURCE_BY_URL);
        q.setParameter("name", PredefinedResourceNames.CONTENT);
        final Object singleResult = q.getSingleResult();

        final Folder contentRoot = Folder.class.cast(singleResult);
        return contentRoot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void createContent(final String pathString) {
        final ResourcePath path = new ResourcePath(pathString);
        final Folder parentFolder = createFoldersForPath(path.elementsToTop());

        final List<ResourceName> elements = path.elements();
        final ResourceName name = elements.get(elements.size()-1);

        boolean resourceExists = true;
        boolean resourceIsFolder = false;

        try {
            final Resource resource = parentFolder.findEntryByName(name);
            resourceIsFolder = resource.type()==ResourceType.FOLDER;
        } catch(final CCCException e) {
            resourceExists = false;
        }

        if (resourceExists && resourceIsFolder) {
            throw new CCCException(
                "A folder already exists at the path "+pathString);
        } else if (!resourceExists) {
            final Page newContent = new Page(name);
            _em.persist(newContent);
            parentFolder.add(newContent);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void createParagraphsForContent(
                                   final String pathString,
                                   final Map<String, Paragraph> paragraphs) {

        final Page page =
            lookup(new ResourcePath(pathString)).asPage();

        for (final String key : paragraphs.keySet()) {
            final Paragraph paragraph = paragraphs.get(key);
            page.addParagraph(key, paragraph);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Resource lookup(final UUID id) {
       return _em.find(Resource.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveContent(final String id,
                            final String title,
                            final Map<String, String> paragraphs) {

        final Page page = lookup(UUID.fromString(id)).asPage();
        page.title(title);
        page.deleteAllParagraphs();

        for (final String key : paragraphs.keySet()) {
            page.addParagraph(key, new Paragraph(paragraphs.get(key)));
        }
    }
}
