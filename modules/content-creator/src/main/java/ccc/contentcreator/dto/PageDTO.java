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

import java.util.Map;


/**
 * A dto for a CCC page.
 *
 * @author Civic Computing Ltd.
 */
public class PageDTO extends ResourceDTO {


    @SuppressWarnings("unused") // Required for GWT
    private PageDTO() { super(null, null, null, null); }

    /**
     * Constructor.
     *
     * @param title The title of the resource.
     * @param id The uuid for the resource.
     * @param name The name of the resource.
     * @param type The type of the resource.
     * @param paragraphs The paragraphs of the page.
     */
    public PageDTO(final String id,
                   final String type,
                   final String name,
                   final String title,
                   final Map<String, String> paragraphs) {
        super(id, type, name, title);
        set("paras", paragraphs);
    }

    /**
     * Accessor for the paragraphs property.
     *
     * @return A map representing the page's paragraphs.
     */
    public Map<String, String> getParagraphs() {
        return get("paras");
    }

}
