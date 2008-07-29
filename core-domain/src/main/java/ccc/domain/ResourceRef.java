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

import ccc.commons.jee.JSON;
import ccc.commons.jee.UID;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class ResourceRef implements JSONable {

    private ResourceName name;
    private UID          id;
    private ResourceType type;

    /**
     * Constructor.
     *
     * @param name
     * @param id
     */
    public ResourceRef(final ResourceName name, final UID id, final ResourceType type) {

        this.name = name;
        this.id = id;
        this.type = type;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ResourceName name() {
        return name;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public UID id() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toJSON() {

        return
            JSON.object()
                .add("name", name.toString())
                .add("id", id.toString())
                .add("type", type.toString())
                .toString();
    }

}
