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


    /**
     * Constructor.
     * This constructor requires values for id and version fields, it can
     * be used to represent persisted (existing) resources.
     *
     * @param id The uuid for the resource.
     * @param version The version of the resource.
     * @param title The title of the resource.
     * @param name The name of the resource.
     * @param type The type of the resource.
     */
    public ResourceDTO(final String id,
                       final int version,
                       final String type,
                       final String name,
                       final String title,
                       final String locked,
                       final String published,
                       final String tags) {

        set("id", id);
        set("version", version);
        set("type", type);
        set("name", name);
        set("title", title);
        set("locked", locked);
        set("tags", tags);
        set("published", published);
    }

    /**
     * Constructor.
     * This constructor will set empty values for id and version fields, it can
     * be used to represent unpersisted (new) resources.
     *
     * @param title The title of the resource.
     * @param name The name of the resource.
     * @param type The type of the resource.
     */
    public ResourceDTO(final String type,
                       final String name,
                       final String title,
                       final String locked,
                       final String published) {
        this(null, -1, type, name, title, locked, published, "");
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

    /**
     * Accessor for type.
     *
     * @return The type as a string.
     */
    public Object getType() {
        return get("type");
    }

    /**
     * Accessor for locked.
     *
     * @return The locked as a string.
     */
    public Object getLocked() {
        return get("locked");
    }
}
