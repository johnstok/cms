/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;
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
