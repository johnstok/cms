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
package ccc.api.types;

import java.io.Serializable;


/**
 * A class representing a mime type. Mime types are defined in the following
 * RFCs:
 * <br>http://tools.ietf.org/html/rfc2045
 * <br>http://tools.ietf.org/html/rfc2046
 *
 * @author Civic Computing Ltd.
 */
public class MimeType implements Serializable {
    private String _primaryType;
    private String _subType;

    @SuppressWarnings("unused") private MimeType() { super(); }

    /**
     * Constructor.
     *
     * @param primaryType The primary type.
     * @param subType The sub-type.
     */
    public MimeType(final String primaryType, final String subType) {
        DBC.require().notEmpty(primaryType);
        DBC.require().notEmpty(subType);
        _primaryType = primaryType;
        _subType = subType;
    }


    /**
     * Accessor.
     *
     * @return Returns the primaryType.
     */
    public String getPrimaryType() {
        return _primaryType;
    }


    /**
     * Mutator.
     *
     * @param primaryType The primaryType to set.
     */
    public void setPrimaryType(final String primaryType) {
        DBC.require().notEmpty(primaryType);
        _primaryType = primaryType;
    }


    /**
     * Accessor.
     *
     * @return Returns the sub-type.
     */
    public String getSubType() {
        return _subType;
    }


    /**
     * Mutator.
     *
     * @param subType The sub-type to set.
     */
    public void setSubType(final String subType) {
        DBC.require().notEmpty(subType);
        _subType = subType;
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
            prime
            * result
            + ((_primaryType == null) ? 0 : _primaryType.hashCode());
        result =
            prime
            * result
            + ((_subType == null) ? 0 : _subType.hashCode());
        return result;
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final MimeType other = (MimeType) obj;
        if (_primaryType == null) {
            if (other._primaryType != null) { return false; }
        } else if (!_primaryType.equals(other._primaryType)) { return false; }
        if (_subType == null) {
            if (other._subType != null) { return false; }
        } else if (!_subType.equals(other._subType)) { return false; }
        return true;
    }


    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _primaryType+"/"+_subType;
    }


    /** HTML : MimeType. */
    public static final MimeType HTML =
        new MimeType("text", "html");
    /** BINARY_DATA : MimeType. */
    public static final MimeType BINARY_DATA =
        new MimeType("application", "octet-stream");
    /** JPEG : MimeType. */
    public static final MimeType JPEG =
        new MimeType("image", "jpeg");
    /** TEXT : MimeType. */
    public static final MimeType TEXT =
        new MimeType("text", "plain");
    /** JSON : MimeType. */
    public static final MimeType JSON =
        new MimeType("application", "json");
}
