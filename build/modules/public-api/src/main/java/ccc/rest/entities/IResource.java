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

package ccc.rest.entities;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.types.Duration;
import ccc.types.ResourceName;
import ccc.types.ResourceType;


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
    ResourceName getName();

    /**
     * Accessor for the title.
     *
     * @return The content's title, as a string.
     */
    String getTitle();

    /**
     * Accessor for a resource's tags.
     *
     * @return The tags for this resource as a list.
     */
    Set<String> getTags();

    /**
     * Accessor for 'include in main menu' property.
     *
     * @return True if the resource should be included, false otherwise.
     */
    boolean isInMainMenu();

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
    Date getDateCreated();

    /**
     * Accessor for the date the resource last changed.
     *
     * @return The date the resource last changed.
     */
    Date getDateChanged();

    /**
     * Accessor for the file's description.
     *
     * @return The description as a string.
     */
    String getDescription();

    // -- More

    /**
     * Compute the template for this resource.
     *
     * @return The selected template.
     */
    UUID getTemplate();

    /**
     * Is the resource visible.
     *
     * @return True if the resource is visible, false otherwise.
     */
    boolean isVisible();

    /**
     * Accessor.
     *
     * @return The resource's type.
     */
    ResourceType getType();

    /**
     * Compute the cache duration for this resource.
     *
     * @return The computed duration.
     */
    Duration getCacheDuration();

    /**
     * Accessor.
     *
     * @return The resource's id.
     */
    UUID getId();
    /**
     * Accessor.
     *
     * @return True if the resource is locked, false otherwise.
     */
    boolean isLocked();

    /**
     * Accessor.
     *
     * @return True if the resource is published, false otherwise.
     */
    boolean isPublished();

    /**
     * Accessor.
     *
     * @return The user that locked the resource or false if the resource is not
     *  locked.
     */
    UUID getLockedBy();

    /**
     * Accessor.
     *
     * @return The resource's metadata, as a map.
     */
    Map<String, String> getMetadata();

    /**
     * Accessor.
     *
     * @return The parent folder for the resource.
     */
    UUID getParent();

    /**
     * Accessor.
     *
     * @return The user that published the resource or null if the resource
     *  isn't published.
     */
    UUID getPublishedBy();

    /**
     * Accessor.
     *
     * @return The absolute path to the resource.
     */
    String getAbsolutePath();


    /**
     * Accessor.
     *
     * @return True if the resource requires security privileges to access;
     *  false otherwise.
     */
    boolean isSecure();
}
