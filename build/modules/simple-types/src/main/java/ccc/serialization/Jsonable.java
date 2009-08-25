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
package ccc.serialization;


/**
 * API for serializing a class to JSON.
 *
 * @author Civic Computing Ltd.
 */
public interface Jsonable {

    /**
     * Convert to JSON.
     *
     * @param json The JSON object to write to.
     */
    void toJson(Json json);
}
