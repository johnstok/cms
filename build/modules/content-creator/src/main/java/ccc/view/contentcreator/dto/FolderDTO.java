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




/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDTO extends ResourceDTO implements DTO {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 2612088448615151340L;

    private int _folderCount;

    @SuppressWarnings("unused")
    private FolderDTO() { super(null, null, null, null); }

    /**
     * Constructor.
     *
     * @param id
     * @param type
     * @param name
     * @param title
     */
    public FolderDTO(final String id, final String type, final String name, final String title, final int folderCount) {
        super(id, type, name, title);
        _folderCount = folderCount;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public int getFolderCount() {
        return _folderCount;
    }
}
