/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;



/**
 * A dto for a CCC resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceDTO extends BaseModelData implements DTO {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = -8271277006169824547L;

    /**
     * Constructor.
     *
     * @param title The title of the resource.
     * @param id The uuid for the resource.
     * @param name The name of the resource.
     * @param type The type of the resource.
     */
    public ResourceDTO(final String id,
                       final String type,
                       final String name,
                       final String title) {

        set("id", id);
        set("type", type);
        set("name", name);
        set("title", title);
    }

    @SuppressWarnings("unused") // Required for GWT
    private ResourceDTO() { super(); }

    /**
     * Accessor for the id property.
     *
     * @return The id as a string.
     */
    public String getId() {
        return get("id");
    }

    /**
     * Accessor for the name field.
     *
     * @return The name as a string.
     */
    public String getName() {
        return get("name");
    }

    /**
     * Accessor for the title.
     *
     * @return The title as a string.
     */
    public String getTitle() {
        return get("title");
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getName();
    }
}
