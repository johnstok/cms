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
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceDTO extends BaseModelData implements DTO {

//    private String _id;
//    private String _type;
//    private String _name;
//    private String _title;

    /**
     * Constructor.
     *
     * @param title
     */
    public ResourceDTO(final String id,
                       final String type,
                       final String name,
                       final String title) {
//        _id = id;
//        _type = type;
//        _name = name;
//        _title = title;
        set("id", id);
        set("type", type);
        set("name", name);
        set("title", title);
    }

    @SuppressWarnings("unused")
    private ResourceDTO() { super(); }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getId() {
        return get("id");
    }


    public String getName() {
        return get("name");
    }

    public String getTitle() {
        return get("title");
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getName();
    }
}
