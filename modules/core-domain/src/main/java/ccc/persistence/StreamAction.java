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

package ccc.persistence;

import java.io.InputStream;

/**
 * An action to perform on an {@link InputStream}.
 *
 * @author Civic Computing Ltd.
 */
public interface StreamAction {
    /**
     * Execute the action.
     *
     * @param is The input stream to operate on.
     * @throws Exception If the action fails.
     */
    void execute(InputStream is) throws Exception;
}
