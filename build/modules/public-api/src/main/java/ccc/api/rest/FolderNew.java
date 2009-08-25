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
package ccc.api.rest;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.ID;


/**
 * A new folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderNew implements Jsonable {

    private final ID     _parentId;
    private final String _name;


    /**
     * Constructor.
     *
     * @param parentId The folder's parent.
     * @param name     The folder's name.
     */
    public FolderNew(final ID parentId, final String name) {
        _parentId = parentId;
        _name = name;
    }


    /**
     * Accessor.
     *
     * @return Returns the parentId.
     */
    public final ID getParentId() {
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
