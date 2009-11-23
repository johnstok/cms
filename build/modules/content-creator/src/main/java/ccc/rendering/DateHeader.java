/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rendering;

import java.text.DateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;


/**
 * A response header with a date value.
 *
 * @author Civic Computing Ltd.
 */
public class DateHeader
    implements
        Header {
    private final String _name;
    private final Date _value;

    /**
     * Constructor.
     *
     * @param name The name of the header.
     * @param value The value of the header.
     */
    public DateHeader(final String name, final Date value) {
        _name = name;
        _value = new Date(value.getTime());
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final HttpServletResponse response) {
        response.setDateHeader(_name, _value.getTime());
    }



    /** {@inheritDoc} */
    @Override
    public String toString() {
        return
            _name
            + ": "
            + DateFormat.getDateTimeInstance().format(_value);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        result = prime * result + ((_value == null) ? 0 : _value.hashCode());
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
        final DateHeader other = (DateHeader) obj;
        if (_name == null) {
            if (other._name != null) {
                return false;
            }
        } else if (!_name.equals(other._name)) {
            return false;
        }
        if (_value == null) {
            if (other._value != null) {
                return false;
            }
        } else if (!_value.equals(other._value)) {
            return false;
        }
        return true;
    }
}
