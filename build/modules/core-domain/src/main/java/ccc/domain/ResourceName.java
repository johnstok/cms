/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

import java.io.Serializable;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * Represents a valid resource name in CCC. A valid name must contain one or
 * more characters. Characters must be members of the group [a-zA-Z_0-9].
 *
 * See the following links for further details on URL encoding:
 * http://en.wikipedia.org/wiki/Percent-encoding
 * http://i-technica.com/whitestuff/urlencodechart.html
 *
 * @author Civic Computing Ltd
 */
public final class ResourceName implements Serializable {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 8023247880499438161L;
    private String _representation = escapeString(UUID.randomUUID().toString());
    public static final String  _validCharacters = "[\\.\\w]+";
    private final Pattern _validRegex = Pattern.compile(_validCharacters);

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    private ResourceName() { /* NO-OP */}

    /**
     * Constructor.
     *
     * @param stringRepresentation
     *  The representation of this name - as a string.
     */
    public ResourceName(final String stringRepresentation) {

        if (null == stringRepresentation) {
            throw new RuntimeException("A resource name may not be NULL.");
        }
        if (stringRepresentation.length() < 1) {
            throw new RuntimeException(
                "A resource name must be longer than zero characters.");
        }
        if (!_validRegex.matcher(stringRepresentation).matches()) {
            throw new RuntimeException(
                stringRepresentation
                + " does not match the java.util.regex pattern '"
                + _validCharacters + "'.");
        }

        _representation = stringRepresentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return _representation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result =
            prime
            * result
            + ((_representation == null) ? 0 : _representation.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final ResourceName other = (ResourceName) obj;
        if (_representation == null) {
            if (other._representation != null) { return false; }
        } else if (!_representation.equals(other._representation)) {
            return false;
        }
        return true;
    }

    /**
     * Escape a string to provide a valid ResourceName.
     *
     * @param invalidCharacters A string that may contain invalid characters.
     * @return A resource name representing the string, where all invalid
     *      characters have been escaped to '_'.
     */
    public static ResourceName escape(final String invalidCharacters) {
        final String validCharacters = escapeString(invalidCharacters);
        return new ResourceName(validCharacters);
    }

    private static String escapeString(final String invalidCharacters) {
        return invalidCharacters.replaceAll("[^\\.\\w]", "_");
    }
}
