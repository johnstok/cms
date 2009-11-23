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

import ccc.rest.snapshots.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.ResourceName;


/**
 * A new alias.
 *
 * @author Civic Computing Ltd.
 */
public class AliasDto
    extends
        ResourceSnapshot
    implements
        Jsonable,
        Serializable {

    private final UUID _targetId;
    private String _targetPath;


    /**
     * Constructor.
     *
     * @param parentId The alias' parent.
     * @param name The alias' name.
     * @param targetId The alias' target.
     */
    public AliasDto(final UUID parentId,
                    final ResourceName name,
                    final UUID targetId) {
        setParent(parentId);
        setName(name);
        _targetId = targetId;
    }


    /**
     * Accessor.
     *
     * @return Returns the targetId.
     */
    public final UUID getTargetId() {
        return _targetId;
    }


    /**
     * Mutator.
     *
     * @param targetPath The targetPath to set.
     */
    public void setTargetPath(final String targetPath) {
        _targetPath = targetPath;
    }


    /**
     * Accessor.
     *
     * @return Returns the targetPath.
     */
    public String getTargetPath() {
        return _targetPath;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, getParent());
        json.set(JsonKeys.NAME, getName().toString());
        json.set(JsonKeys.TARGET_ID, _targetId);
    }
}
