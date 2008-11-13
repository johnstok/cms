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

/**
 * The data class is used to represent raw binary data with a unique identifier.
 * Typically, an instance of this class will be passed to some data manager in
 * order to open an input stream on the bytes themselves.
 *
 * @author Civic Computing Ltd.
 */
public class Data extends VersionedEntity {

    /** Constructor: for persistence only. */
    public Data() { super(); } // TODO: Should be protected?

}
