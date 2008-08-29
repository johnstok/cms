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


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Maybe<T> {

    private final T _value;

    /**
     * Constructor.
     *
     * @param object
     */
    public Maybe(final T value) {
        _value = value;
    }

    /**
     * Constructor.
     *
     */
    public Maybe() {
        _value = null;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public boolean isPresent() {
        return !(null==_value);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public T get() {
        if (isPresent()) {
            return _value;
        }
        throw new IllegalStateException("Maybe does not contain a value.");
    }

}
