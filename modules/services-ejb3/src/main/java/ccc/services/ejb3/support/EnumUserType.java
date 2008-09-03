/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3.support;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;


/**
 * TODO Add Description for this type.
 *
 * @param <T> The type of the enum to persist.
 *
 * @author Civic Computing Ltd.
 */
public class EnumUserType<T extends Enum<T>> implements UserType,
                                                        ParameterizedType {

    private Class<T> _enumClass;

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void setParameterValues(final Properties parameters) {
        final String enumClassName = parameters.getProperty("type");
        try {
            _enumClass = (Class<T>) Class.forName(enumClassName);
        } catch (final ClassNotFoundException cnfe) {
            throw new HibernateException("Enum class not found.", cnfe);
        }
    }

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
        return (Serializable) value;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object x, final Object y) {
        return x==y;
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
        return Enum.valueOf(this._enumClass, value);

    }

    /** {@inheritDoc} */
    @Override
    public void nullSafeSet(final PreparedStatement st,
                            final Object value,
                            final int index) throws SQLException {
        if (value==null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, _enumClass.cast(value).name());
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
    public Class<T> returnedClass() {
        return _enumClass;
    }

    /** {@inheritDoc} */
    @Override
    public int[] sqlTypes() {
        return new int[] {Types.VARCHAR};
    }

}
