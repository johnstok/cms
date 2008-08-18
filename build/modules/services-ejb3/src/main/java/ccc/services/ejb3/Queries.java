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

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ccc.domain.Folder;
import ccc.domain.ResourceName;


/**
 * Queries used by the business layer.
 *
 * @author Civic Computing Ltd
 */
public final class Queries {

    /**
     * Constructor.
     */
    Queries() { /* NO-OP */ }

    /**
     * Look up the root folder for the content hierarchy.
     *
     * @param em The entity manager used to look up the root.
     * @param name The name of the resource.
     * @return The folder with the specified name.
     */
    protected Folder lookupRoot(final EntityManager em,
                                 final ResourceName name) {

        final Query q = em.createNamedQuery(RESOURCE_BY_URL);
        q.setParameter("name", name);
        final Object singleResult = q.getSingleResult();

        final Folder folder = Folder.class.cast(singleResult);
        return folder;
    }

    /** RESOURCE_BY_URL : String. */
    public static final String RESOURCE_BY_URL = "RESOURCE_BY_URL";
}
