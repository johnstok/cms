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
package ccc.commons;

import java.io.Serializable;


/**
 * This class is a trivial, incomplete expression of the 'Maybe' monad found
 * in functional programming languages such as haskell. The purpose of this
 * class is to allow a non-null value to be returned from methods whose
 * behaviour may in some cases be undefined.
 *
 * An instance of Maybe can be queried to determine whether it contains a value.
 *
 * <br><br>For more information see:
 * <br>http://blog.tmorris.net/maybe-in-java/
 * <br>http://rickyclarkson.blogspot.com/2008/07/optional-values-in-java.html
 * <br>http://en.wikipedia.org/wiki/Monad_(functional_programming)#Maybe_monad
 *
 * @param <T> The type of the value that this maybe represents.
 *
 * @author Civic Computing Ltd.
 */
public class Maybe<T extends Serializable> implements Serializable {

    private final T _value;

    /**
     * Constructor.
     *
     * @param value The value this maybe will represent. If null is passed this
     * maybe is considered to represent undefined value.
     */
    public Maybe(final T value) {
        _value = value;
    }

    /**
     * Constructor. Create a maybe that represents an undefined value.
     *
     */
    public Maybe() {
        this(null);
    }

    /**
     * Determine whether this maybe represents a defined value.
     *
     * @return True if a value is defined false otherwise.
     */
    public boolean isDefined() {
        return !(null==_value);
    }

    /**
     * Retrieve the value this maybe represents. Throws an IllegalStateException
     * if the maybe represents an undefined value.
     *
     * @return The value represented by the maybe.
     */
    public T get() {
        if (isDefined()) {
            return _value;
        }
        throw new IllegalStateException("Maybe does not contain a value.");
    }

}
