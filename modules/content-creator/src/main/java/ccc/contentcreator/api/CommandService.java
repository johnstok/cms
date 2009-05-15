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
package ccc.contentcreator.api;

import ccc.api.Commands;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * Command API. Methods implemented by this API will change data in the
 * underlying system.
 *
 * @author Civic Computing Ltd.
 */
@RemoteServiceRelativePath("commands")
public interface CommandService
    extends
        Commands, RemoteService {

    /* No further methods */

}
