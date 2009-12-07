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

import ccc.rest.snapshots.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.ResourceName;


/**
 * A new folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDto
    extends
        ResourceSnapshot
    implements
        Jsonable,
        Serializable {

    private UUID _indexPage;
    private UUID _defaultPage;


    /**
     * Constructor.
     *
     * @param parentId The folder's parent.
     * @param name     The folder's name.
     */
    public FolderDto(final UUID parentId, final ResourceName name) {
        setParent(parentId);
        setName(name);
    }


    /**
     * Accessor.
     *
     * @return Returns the indexPage.
     */
    public UUID getIndexPage() {
        return _indexPage;
    }


    /**
     * Mutator.
     *
     * @param indexPage The indexPage to set.
     */
    public void setIndexPage(final UUID indexPage) {
        _indexPage = indexPage;
    }


    /**
     * Mutator.
     *
     * @param defaultPage The defaultPage to set.
     */
    public void setDefaultPage(final UUID defaultPage) {
        _defaultPage = defaultPage;
    }


    /**
     * Accessor.
     *
     * @return Returns the defaultPage.
     */
    public UUID getDefaultPage() {
        return _defaultPage;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, getParent());
        json.set(JsonKeys.NAME, getName().toString());
    }
}
