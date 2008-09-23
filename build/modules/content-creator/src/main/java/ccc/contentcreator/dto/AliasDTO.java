/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dto;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class AliasDTO extends ResourceDTO {

    @SuppressWarnings("unused") // Required for GWT
    private AliasDTO() { super(null, null, null, null); }

    /**
     * Constructor.
     *
     * @param title The title of the resource.
     * @param id The uuid for the resource.
     * @param name The name of the resource.
     * @param type The type of the resource.
     * @param folderCount The number of folder contained by this folder.
     */
    public AliasDTO(final String id,
                     final String type,
                     final String name,
                     final String title,
                     final String targetId) {
        super(id, type, name, title);
        set("targetId", targetId);

    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getTargetId() {
        return get("targetId");
    }
}
