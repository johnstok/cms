/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.api.types;

import java.util.ArrayList;
import java.util.List;

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;


/**
 * Wrapper class for a collection of DTOs.
 *
 * @param <T> The type of the elements.
 *
 * @author Civic Computing Ltd.
 */
// FIXME: Make abstract?
// Reify the generic type parameter on s11n.
public class PagedCollection<T extends Jsonable>
    extends
        Res {

    protected long _totalCount;
    protected List<T> _elements;


    /**
     * Constructor.
     *
     * @param totalCount The total number of elements in the collection.
     * @param elements The elements on the current page.
     */
    public PagedCollection(final long totalCount,
                           final List<T> elements) {
        _totalCount = totalCount;
        _elements = elements;
    }


    /**
     * Constructor.
     */
    public PagedCollection() {
        _totalCount = 0;
        _elements = new ArrayList<T>();
    }


    /**
     * Accessor.
     *
     * @return Returns the total count.
     */
    public final long getTotalCount() {

        return _totalCount;
    }


    /**
     * Accessor.
     *
     * @return Returns the elements.
     */
    public final List<T> getElements() {
        return _elements;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.SIZE, Long.valueOf(_totalCount));
        json.set(JsonKeys.ELEMENTS, _elements);
    }
}
