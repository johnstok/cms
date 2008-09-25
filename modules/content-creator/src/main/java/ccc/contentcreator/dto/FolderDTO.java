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




/**
 * A dto for a CCC folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDTO extends ResourceDTO {


    @SuppressWarnings("unused") // Required for GWT
    private FolderDTO() { super(null, null, null); }

    /**
     * Constructor.
     *
     * @param id The uuid for the resource.
     * @param version The version of the resource.
     * @param name The name of the resource.
     * @param title The title of the resource.
     * @param folderCount The number of folder contained by this folder.
     */
    public FolderDTO(final String id,
                     final int version,
                     final String name,
                     final String title,
                     final int folderCount) {

        super(id, version, "FOLDER", name, title);
        set("folderCount", folderCount);
    }

    /**
     * Accessor method for the folderCount property.
     *
     * @return The number of folders as an integer.
     */
    public Integer getFolderCount() {
        return get("folderCount");
    }
}
