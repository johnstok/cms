/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.entities;

import java.util.Date;
import java.util.Set;

import ccc.types.ResourceName;


/**
 * API for a resource.
 *
 * @author Civic Computing Ltd.
 */
public interface IResource {

    /**
     * Accessor for name.
     *
     * @return The name for this resource, as a {@link ResourceName}.
     */
    ResourceName name();

    /**
     * Accessor for the title.
     *
     * @return The content's title, as a string.
     */
    String title();

    /**
     * Accessor for a resource's tags.
     *
     * @return The tags for this resource as a list.
     */
    Set<String> tags();

    /**
     * Accessor for 'include in main menu' property.
     *
     * @return True if the resource should be included, false otherwise.
     */
    boolean includeInMainMenu();

    /**
     * Retrieve metadata for this resource. If this resource does not contain
     * an entry for the specified key parent resources will be recursively
     * checked.
     *
     * @param key The key with which the datum was stored.
     * @return The value of the datum. NULL if the datum doesn't exist.
     */
    String getMetadatum(final String key);

    /**
     * Accessor for the date the resource was created.
     *
     * @return The date of creation.
     */
    Date dateCreated();

    /**
     * Accessor for the date the resource last changed.
     *
     * @return The date the resource last changed.
     */
    Date dateChanged();

    /**
     * Accessor for the file's description.
     *
     * @return The description as a string.
     */
    String description();

}
