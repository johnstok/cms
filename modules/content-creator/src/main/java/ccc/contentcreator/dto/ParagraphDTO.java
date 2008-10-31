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
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ParagraphDTO extends BaseModelData implements DTO {

    /**
     * Constructor.
     *
     * @param type The type as a string.
     * @param value The value as a string.
     */
    public ParagraphDTO(final String type, final String value) {
        set("type", type);
        set("value", value);
    }

    @SuppressWarnings("unused") // Required for GWT
    private ParagraphDTO() { super(); }

    /**
     * Accessor for type.
     *
     * @return The type as a string.
     */
    public String getType() {
        return get("type");
    }

    /**
     * Accessor for value.
     *
     * @return The value as a string.
     */
    public String getValue() {
        return get("value");
    }
}
