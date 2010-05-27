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
package ccc.api.core;

import java.util.ArrayList;
import java.util.List;


/**
 * Wrapper class for a collection of DTOs.
 *
 * @param <T> The type of the elements.
 *
 * @author Civic Computing Ltd.
 */
public class PagedCollection<T>
    extends
        Res {

    private long         _totalCount;
    private List<T>      _elements;
    private final Class<T> _elementClass;


    /**
     * Constructor.
     *
     * @param totalCount The total number of elements in the collection.
     * @param elementClass The type of collection to create.
     * @param elements The elements on the current page.
     */
    public PagedCollection(final long totalCount,
                           final Class<T> elementClass,
                           final List<T> elements) {
        _totalCount = totalCount;
        _elements = elements;
        _elementClass = elementClass;
    }


    /**
     * Constructor.
     *
     * @param elementClass The type of collection to create.
     */
    public PagedCollection(final Class<T> elementClass) {
        _totalCount = 0;
        _elements = new ArrayList<T>();
        _elementClass = elementClass;
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


    /**
     * Accessor.
     *
     * @return Returns the elementClass.
     */
    public Class<T> getElementClass() {
        return _elementClass;
    }
}
