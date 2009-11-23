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

import javax.servlet.http.HttpServletResponse;


/**
 * A response header with an integer value.
 *
 * @author Civic Computing Ltd.
 */
public class IntHeader
    implements
        Header {

    private final int _value;
    private final String _name;

    /**
     * Constructor.
     *
     * @param value The value of the header.
     * @param name The name of the header.
     */
    public IntHeader(final String name, final int value) {
        _value = value;
        _name = name;
    }


    /** {@inheritDoc} */
    @Override
    public void writeTo(final HttpServletResponse response) {
        response.setIntHeader(_name, _value);
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        result = prime * result + _value;
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
        final IntHeader other = (IntHeader) obj;
        if (_name == null) {
            if (other._name != null) {
                return false;
            }
        } else if (!_name.equals(other._name)) {
            return false;
        }
        if (_value != other._value) {
            return false;
        }
        return true;
    }
}
