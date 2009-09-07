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
 * A new alias.
 *
 * @author Civic Computing Ltd.
 */
public class AliasDto implements Jsonable, Serializable {

    private final UUID _parentId;
    private final String _name;
    private final UUID _targetId;


    /**
     * Constructor.
     *
     * @param parentId The alias' parent.
     * @param name The alias' name.
     * @param targetId The alias' target.
     */
    public AliasDto(final UUID parentId,
                    final String name,
                    final UUID targetId) {
        _parentId = parentId;
        _name = name;
        _targetId = targetId;
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


    /**
     * Accessor.
     *
     * @return Returns the targetId.
     */
    public final UUID getTargetId() {
        return _targetId;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, _parentId);
        json.set(JsonKeys.NAME, _name);
        json.set(JsonKeys.TARGET_ID, _targetId);
    }
}
