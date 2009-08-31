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
package ccc.rest;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.ID;


/**
 * A new alias.
 *
 * @author Civic Computing Ltd.
 */
public class AliasNew implements Jsonable {

    private final ID _parentId;
    private final String _name;
    private final ID _targetId;


    /**
     * Constructor.
     *
     * @param parentId The alias' parent.
     * @param name The alias' name.
     * @param targetId The alias' target.
     */
    public AliasNew(final ID parentId, final String name, final ID targetId) {
        _parentId = parentId;
        _name = name;
        _targetId = targetId;
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


    /**
     * Accessor.
     *
     * @return Returns the targetId.
     */
    public final ID getTargetId() {
        return _targetId;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, _parentId);
        json.set(JsonKeys.NAME, _name);
        json.set(JsonKeys.TARGET, _targetId);
    }
}
