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

package ccc.api.template;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * A read only view onto a resource.
 *
 * @author Civic Computing Ltd.
 */
public interface IResourceSnapshot {

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    String description();

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    String name();

    /**
     * @return
     * @see ccc.domain.Resource#dateChanged()
     */
    Date dateChanged();

    /**
     * @return
     * @see ccc.domain.Resource#dateCreated()
     */
    Date dateCreated();

    /**
     * @param key
     * @return
     * @see ccc.domain.Resource#getMetadatum(java.lang.String)
     */
    String getMetadatum(final String key);

    /**
     * @return
     * @see ccc.domain.Resource#includeInMainMenu()
     */
    boolean includeInMainMenu();

    /**
     * @return
     * @see ccc.domain.Resource#tags()
     */
    Set<String> tags();

    /**
     * @return
     * @see ccc.domain.Resource#title()
     */
    String title();

    List<IResourceSnapshot> selectPathElements();

}