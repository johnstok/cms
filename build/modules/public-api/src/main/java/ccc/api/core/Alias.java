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
package ccc.api.core;

import java.util.UUID;

import ccc.api.types.ResourceName;


/**
 * A new alias.
 *
 * @author Civic Computing Ltd.
 */
public class Alias
    extends
        Resource {

    private UUID _targetId;
    private String _targetPath;


    /**
     * Constructor.
     *
     * @param parentId The alias' parent.
     * @param name The alias' name.
     * @param targetId The alias' target.
     */
    @Deprecated
    public Alias(final UUID parentId,
                    final ResourceName name,
                    final UUID targetId) {
        setParent(parentId);
        setName(name);
        _targetId = targetId;
    }


    /**
     * Constructor.
     *
     * @param targetId The alias' target's id.
     */
    @Deprecated
    public Alias(final UUID targetId) {
        _targetId = targetId;
    }


    /**
     * Constructor.
     */
    public Alias() { super(); }


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
     * @param targetId The targetId to set.
     */
    public void setTargetId(final UUID targetId) {
        _targetId = targetId;
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


    /** TARGET_NAME : String. */
    public static final String TARGET_NAME = "target_name";
}
