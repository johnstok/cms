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

import static ccc.commons.jee.DBC.require;

/**
 * An abstract superclass that contains shared behaviour for the different types
 * of CCC resource.
 *
 * @author Civic Computing Ltd
 */
public abstract class Resource extends Entity {

    private String       title = id().toString();
    private ResourceName name  = ResourceName.escape(title);

    /**
     * Constructor.
     * Sets the resource's title to be the same as the name.
     *
     * @param name The name for this resource.
     */
    protected Resource(final ResourceName name) {
        require().notNull(name);
        this.name = name;
        title = name.toString();
    }

    /**
     * Constructor.
     *
     * @param name
     * @param title
     */
    protected Resource(final ResourceName name, final String title) {
        require().notNull(name);
        require().notEmpty(title);
        this.name = name;
        this.title = title;
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
    public String title() {
        return title;
    }
}
