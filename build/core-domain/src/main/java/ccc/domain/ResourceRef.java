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

package ccc.domain;

import java.util.UUID;

import ccc.commons.jee.JSON;


/**
 * This class models a reference to a resource.
 *
 * @author Civic Computing Ltd
 */
public class ResourceRef implements JSONable {

    private ResourceName name;
    private UUID         id;
    private ResourceType type;

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param id The unique ID for the resource.
     * @param type The type of the resource.
     */
    public ResourceRef(final ResourceName name,
                       final UUID id,
                       final ResourceType type) {

        this.name = name;
        this.id = id;
        this.type = type;
    }

    /**
     * Accessor for the name field.
     *
     * @return The name as a {@link ResourceName}.
     */
    public final ResourceName name() {
        return name;
    }

    /**
     * Accessor for the ID field.
     *
     * @return The ID as a {@link UUID}.
     */
    public final UUID id() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toJSON() {
        return
            JSON.object()
                .add("name", name.toString())
                .add("id", id.toString())
                .add("type", type.toString())
                .toString();
    }

}
