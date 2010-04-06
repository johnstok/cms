/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import ccc.rest.dto.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.ResourceType;


/**
 * A search that can return other results.
 *
 * @author Civic Computing Ltd.
 */
public class Search
    extends
        Resource {

    /** Constructor: for persistence only. */
    protected Search() { super(); }


    /**
     * Constructor.
     *
     * @param title The title for this resource.
     */
    public Search(final String title) {
        super(title);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceType getType() {
        return ResourceType.SEARCH;
    }


    /** {@inheritDoc} */
    @Override
    public Jsonable createSnapshot() {
        return new Jsonable(){
            /** {@inheritDoc} */
            @Override public void toJson(final Json json) {
                json.set(JsonKeys.TITLE, getTitle());
            }
        };
    }


    /** {@inheritDoc} */
    @Override
    public final ResourceSnapshot forCurrentRevision() {
        final ResourceSnapshot dto = new ResourceSnapshot();
        setDtoProps(dto);
        return dto;
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot forSpecificRevision(final int revNo) {
        final ResourceSnapshot dto = new ResourceSnapshot();
        setDtoProps(dto);
        return dto;
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot forWorkingCopy() {
        final ResourceSnapshot dto = new ResourceSnapshot();
        setDtoProps(dto);
        return dto;
    }

}
