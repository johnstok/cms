/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;


/**
 * Simple API for editable objects.
 *
 * @author Civic Computing Ltd.
 */
public interface Editable {

    /**
     * Confirm the edits.
     */
    void save();


    /**
     * Discard the edits.
     */
    void cancel();
}
