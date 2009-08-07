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

package ccc.domain;

import java.util.UUID;

import ccc.entities.IData;

/**
 * The data class is used to represent raw binary data with a unique identifier.
 * Typically, an instance of this class will be passed to some data manager in
 * order to open an input stream on the bytes themselves.
 *
 * @author Civic Computing Ltd.
 */
public class Data extends Entity implements IData {

    /** Constructor: for persistence only. */
    public Data() { super(); }

    /**
     * Constructor.
     *
     * @param id The uuid for this data.
     */
    public Data(final UUID id) {
        id(id);
    }

}
