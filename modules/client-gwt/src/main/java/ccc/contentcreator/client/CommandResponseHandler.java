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
 * TODO: Add a description for this type.
 *
 * @param <T> The type of response returned from a successful execution.
 *
 * @author Civic Computing Ltd.
 */
public interface CommandResponseHandler<T> {

    /**
     * Handle a successful command invocation.
     *
     * @param response The command response.
     */
    void onSuccess(T response);
}
