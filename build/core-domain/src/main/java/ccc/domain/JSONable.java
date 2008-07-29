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


/**
 * Interface describing the API for JSON support.
 *
 * @author Civic Computing Ltd
 */
public interface JSONable {

    /**
     * Serialise to JSON.
     *
     * @return A JSON representation of this object as a String
     */
    public String toJSON();
}
