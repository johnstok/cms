/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.dto;

import java.io.Serializable;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;


/**
 * A new folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderNew implements Jsonable, Serializable {

    private final UUID     _parentId;
    private final String _name;


    /**
     * Constructor.
     *
     * @param parentId The folder's parent.
     * @param name     The folder's name.
     */
    public FolderNew(final UUID parentId, final String name) {
        _parentId = parentId;
        _name = name;
    }


    /**
     * Accessor.
     *
     * @return Returns the parentId.
     */
    public final UUID getParentId() {
        return _parentId;
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public final String getName() {
        return _name;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, _parentId);
        json.set(JsonKeys.NAME, _name);
    }
}
