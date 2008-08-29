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
package ccc.view.contentcreator.dto;

import java.util.Map;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageDTO extends ResourceDTO implements DTO {

    @SuppressWarnings("unused")
    private PageDTO() { super(null, null, null, null); }

    /**
     * Constructor.
     *
     * @param id
     * @param type
     * @param name
     * @param title
     */
    public PageDTO(final String id, final String type, final String name, final String title) {
        super(id, type, name, title);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Map<String, String> getParagraphs() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

}
