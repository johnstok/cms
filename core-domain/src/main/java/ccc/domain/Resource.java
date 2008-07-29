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

package ccc.domain;

import static ccc.commons.jee.DBC.*;

/**
 * An abstract superclass that contains shared behaviour for the different types
 * of CCC resource.
 *
 * @author Civic Computing Ltd
 */
public abstract class Resource extends Entity implements JSONable {

    private String       title = id().toString();
    private ResourceName name  = ResourceName.escape(title);

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    protected Resource() { /* NO-OP */ }

    /**
     * Constructor.
     * Sets the resource's title to be the same as the name.
     *
     * @param resourceName The name for this resource.
     */
    protected Resource(final ResourceName resourceName) {
        require().notNull(resourceName);
        name = resourceName;
        title = resourceName.toString();
    }

    /**
     * Constructor.
     *
     * @param resourceName The name for this resource.
     * @param titleString The title of this resource as a string.
     */
    protected Resource(final ResourceName resourceName,
                       final String titleString) {
        require().notNull(resourceName);
        require().notEmpty(titleString);
        name = resourceName;
        title = titleString;
    }

    /**
     * Query the type of this resource.
     *
     * @return The ResourceType that describes this resource.
     */
    public abstract ResourceType type();

    /**
     * Type-safe helper method to convert an instance of {@link Resource} to an
     * instance of {@link Content}.
     *
     * @return This resource as Content.
     */
    public final Content asContent() {
        return Content.class.cast(this);
    }

    /**
     * Type-safe helper method to convert an instance of {@link Resource} to an
     * instance of {@link Folder}.
     *
     * @return This resource as a Folder.
     */
    public final Folder asFolder() {
        return Folder.class.cast(this);
    }

    /**
     * Accessor for name.
     *
     * @return The name for this resource, as a {@link ResourceName}.
     */
    public final ResourceName name() {
        return name;
    }

    /**
     * Accessor for the title.
     *
     * @return The content's title, as a string.
     */
    public final String title() {
        return title;
    }
}
