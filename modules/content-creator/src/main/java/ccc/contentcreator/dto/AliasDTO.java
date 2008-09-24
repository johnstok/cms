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
    private AliasDTO() { super(null, null, null); }

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param title The title of the resource.
     * @param targetId The uuid for the alias' target.
     */
    public AliasDTO(final String name,
                    final String title,
                    final String targetId) {
        super("ALIAS", name, title);
        set("targetId", targetId);
    }

    /**
     * Constructor.
     *
     * @param id The uuid for the resource.
     * @param version The version of the resource.
     * @param name The name of the resource.
     * @param title The title of the resource.
     * @param targetId The uuid for the alias' target.
     */
    public AliasDTO(final String id,
                    final int version,
                    final String name,
                    final String title,
                    final String targetId) {
        super(id, version, "ALIAS", name, title);
        set("targetId", targetId);
    }

    /**
     * Accessor for the targetId field.
     *
     * @return The uuid of the target, as a String.
     */
    public String getTargetId() {
        return get("targetId");
    }
}
