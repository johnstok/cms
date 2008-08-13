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

import static ccc.commons.DBC.*;

/**
 * An abstract superclass that contains shared behaviour for the different types
 * of CCC resource.
 *
 * @author Civic Computing Ltd
 */
public abstract class Resource extends Entity implements JSONable {

    private String       _title = id().toString();
    private ResourceName _name  = ResourceName.escape(_title);
    private String _displayTemplateName = null;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    protected Resource() { /* NO-OP */ }

    /**
     * Constructor.
     * Sets the resource's title to be the same as the name.
     *
     * @param name The name for this resource.
     */
    protected Resource(final ResourceName name) {
        require().notNull(name);
        _name = name;
        _title = name.toString();
    }

    /**
     * Constructor.
     *
     * @param name The name for this resource.
     * @param title The title of this resource, as a string.
     */
    protected Resource(final ResourceName name,
                       final String title) {
        require().notNull(name);

        _name = name;
        title(title);
    }

    /**
     * Query the type of this resource.
     *
     * @return The ResourceType that describes this resource.
     */
    public abstract ResourceType type();

    /**
     * Type-safe helper method to convert an instance of {@link Resource} to an
     * instance of {@link Page}.
     *
     * @return This resource as a Page.
     */
    public final Page asPage() {
        return Page.class.cast(this);
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
        return _name;
    }

    /**
     * Accessor for the title.
     *
     * @return The content's title, as a string.
     */
    public final String title() {
        return _title;
    }

    /**
     * Sets the title of the resource.
     *
     * @param titleString The new title for this resource.
     */
    public final void title(final String titleString) {
        require().notEmpty(titleString);
        _title = titleString;
    }

    /**
     * Accessor for the display template name.
     *
     * @return The display template name.
     */
    public final String displayTemplateName() {
        return _displayTemplateName;
    }

    /**
     * Sets the display template name of the resource.
     *
     * @param displayTemplateName The new display template name.
     */
    public final void displayTemplateName(final String displayTemplateName) {
        _displayTemplateName = displayTemplateName;
    }
}
