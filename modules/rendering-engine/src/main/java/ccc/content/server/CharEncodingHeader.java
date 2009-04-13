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
package ccc.content.server;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CharEncodingHeader
    implements
        Header {

    private final Charset _value;

    /**
     * Constructor.
     *
     * @param value
     */
    public CharEncodingHeader(final Charset value) {
        _value = value;
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(final HttpServletResponse response) {
        response.setCharacterEncoding(_value.toString());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        final CharEncodingHeader other = (CharEncodingHeader) obj;
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
