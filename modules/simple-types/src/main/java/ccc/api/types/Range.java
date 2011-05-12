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



/**
 * A range of values.
 *
 * Modelled after Martin Fowler's Range pattern:
 * http://martinfowler.com/ap2/range.html
 * The NULL value is interpreted as meaning unlimited.
 *
 * @param <T> The type of range.
 *
 * @author Civic Computing Ltd.
 */
public final class Range<T extends Comparable<T>> {

    private final T        _start;
    private final T        _end;
    private final Class<T> _type;

    /**
     * Constructor.
     *
     * @param clazz Class representing the type of the range.
     * @param start The start of the range (lower bound).
     * @param end   The end of the range (upper bound).
     */
    public Range(final Class<T> clazz, final T start, final T end) {
        // TODO: Require start is <= end
        _type = DBC.require().notNull(clazz);
        _start = start;
        _end = end;
    }


    /**
     * Accessor.
     *
     * @return Returns the start.
     */
    public T getStart() {
        return _start;
    }


    /**
     * Accessor.
     *
     * @return Returns the end.
     */
    public T getEnd() {
        return _end;
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((_end == null) ? 0 : _end.hashCode());
        result = prime * result + ((_start == null) ? 0 : _start.hashCode());
        return result;
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Range<?> other = (Range<?>) obj;
        if (_end == null) {
            if (other._end != null) {
                return false;
            }
        } else if (!_end.equals(other._end)) {
            return false;
        }
        if (_start == null) {
            if (other._start != null) {
                return false;
            }
        } else if (!_start.equals(other._start)) {
            return false;
        }
        return true;
    }


    /**
     * Accessor.
     *
     * @return The class representing the type of this range.
     */
    public Class<T> getType() {
        return _type;
    }
}
