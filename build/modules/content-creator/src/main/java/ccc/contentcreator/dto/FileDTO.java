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
 * A dto for a CCC File.
 *
 * @author Civic Computing Ltd.
 */
public class FileDTO extends ResourceDTO {


    @SuppressWarnings("unused") // Required for GWT
    private FileDTO() { super(null, null, null); }

    /**
     * Constructor.
     *
     * @param id The uuid for the resource.
     * @param version The version of the resource.
     * @param name The name of the resource.
     * @param title The title of the resource.
     * @param size The size of the resource.
     * @param mimeType The mimeType of the resource.
     * @param data The data of the resource.
     * @param description The description of the resource.
     */
    public FileDTO(final String id,
                     final int version,
                     final String name,
                     final String title,
                     final long size,
                     final String mimeType,
                     final String data,
                     final String description) {

        super(id, version, "FILE", name, title);
        set("size", size);
        set("mimeType", mimeType);
        set("data", data);
        set("description", description);
    }

    /**
     * Accessor for size property.
     *
     * @return The size as a Long.
     */
    public Long getSize() {
        return get("size");
    }

    /**
     * Accessor for mimeType property.
     *
     * @return The mime type as a string in the format 'major/minor'.
     */
    public String getMimeType() {
        return get("mimeType");
    }

    /**
     * Accessor for description property.
     *
     * @return The description as a string.
     */
    public String getDescription() {
        return get("description");
    }

}
