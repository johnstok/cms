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

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 2612088448615151340L;

    private int _folderCount;

    @SuppressWarnings("unused") // Required for GWT
    private FolderDTO() { super(null, null, null, null); }

    /**
     * Constructor.
     *
     * @param title The title of the resource.
     * @param id The uuid for the resource.
     * @param name The name of the resource.
     * @param type The type of the resource.
     * @param folderCount The number of folder contained by this folder.
     */
    public FolderDTO(final String id,
                     final String type,
                     final String name,
                     final String title,
                     final int folderCount) {

        super(id, type, name, title);
        _folderCount = folderCount;
    }

    /**
     * Accessor method for the folderCount property.
     *
     * @return The number of folders as an integer.
     */
    public int getFolderCount() {
        return _folderCount;
    }
}
