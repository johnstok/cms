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
public class ContentManagerEJB extends
ManagerSupport implements ContentManager {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED) EntityManager _em;

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
        return
            lookupRoot(_em, PredefinedResourceNames.CONTENT).navigateTo(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void createFolder(final String pathString) {
        final ResourcePath path = new ResourcePath(pathString);
        createFoldersForPath(
            _em, PredefinedResourceNames.CONTENT, path.elements());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void createRoot() {
        try {
            lookupRoot(_em, PredefinedResourceNames.CONTENT);
        } catch (final NoResultException e) {
            _em.persist(new Folder(PredefinedResourceNames.CONTENT));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void createContent(final String pathString,
                                    final String title) {
        final ResourcePath path = new ResourcePath(pathString);
        final Folder parentFolder =
            createFoldersForPath(
                _em, PredefinedResourceNames.CONTENT, path.elementsToTop());

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
            final Page newContent = new Page(name, title);
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
        return super.lookup(_em, id);
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
