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

import static ccc.services.ejb3.Queries.*;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourceName;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class ManagerSupport {

    /**
     * Look up the root folder for the content hierarchy.
     *
     * @param em The entity manager used to look up the root.
     * @param name The name of the resource.
     * @return The folder with the specified name.
     */
    protected final Folder lookupRoot(final EntityManager em,
                                 final ResourceName name) {

        final Query q = em.createNamedQuery(RESOURCE_BY_URL);
        q.setParameter("name", name);
        final Object singleResult = q.getSingleResult();

        final Folder folder = Folder.class.cast(singleResult);
        return folder;
    }

    /**
     * Creates new folders for the path in case folders do not exist
     * already.
     *
     * @param elements
     * @return
     */
    protected Folder createFoldersForPath(final EntityManager em,
                                        final ResourceName rootName,
                                        final List<ResourceName> elements) {

        Folder currentFolder =
            lookupRoot(em, rootName);

        for (final ResourceName name : elements) {
            try {
                currentFolder = currentFolder.findEntryByName(name).asFolder();
            } catch(final CCCException e) {
                final Folder newFolder = new Folder(name);
                em.persist(newFolder);
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
     * TODO: Add a description of this method.
     *
     * @param em
     * @param id
     * @return
     */
    protected final Resource lookup(final EntityManager em, final UUID id) {
       return em.find(Resource.class, id);
    }
}