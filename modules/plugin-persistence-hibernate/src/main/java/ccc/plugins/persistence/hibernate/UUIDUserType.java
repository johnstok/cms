/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.plugins.persistence.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import org.hibernate.usertype.UserType;


/**
 * A hibernate {@link UserType} used to persist {@link UUID} objects.
 *
 * @author Civic Computing Ltd.
 */
public class UUIDUserType implements UserType, Serializable {

    /** {@inheritDoc} */
    @Override
    public Object assemble(final Serializable cached, final Object owner) {
        return cached;
    }

    /** {@inheritDoc} */
    @Override
    public Object deepCopy(final Object value) {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable disassemble(final Object value) {
        return (UUID) value;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object x, final Object y) {
        if (null==x && null==y) {
            return true;
        } else if (null==x) { // y is not null
            return false;
        } else {
            return x.equals(y);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode(final Object x) {
        return x.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMutable() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Object nullSafeGet(final ResultSet rs,
                              final String[] names,
                              final Object owner) throws SQLException {

        final String value = rs.getString(names[0]);
        if (null == value) {
            return null;
        }
        return UUID.fromString(value);

    }

    /** {@inheritDoc} */
    @Override
    public void nullSafeSet(final PreparedStatement st,
                            final Object value,
                            final int index) throws SQLException {
        if (value==null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, ((UUID) value).toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object replace(final Object original,
                          final Object target,
                          final Object owner) {
        return original;
    }

    /** {@inheritDoc} */
    @Override
    public Class<UUID> returnedClass() {
        return UUID.class;
    }

    /** {@inheritDoc} */
    @Override
    public int[] sqlTypes() {
        return new int[] {Types.VARCHAR};
    }
}
