/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services;

import java.util.Set;
import java.util.UUID;

import ccc.domain.Page;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface ISearch {

    /**
     * TODO: Add a description of this method.
     *
     * @param searchTerms
     * @return
     */
    public Set<UUID> find(final String searchTerms);

    /**
     * TODO: Add a description of this method.
     *
     * @param page
     */
    public void index(final Page page);

}
