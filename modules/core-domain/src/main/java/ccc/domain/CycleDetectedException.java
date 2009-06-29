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
package ccc.domain;

import ccc.api.CommandFailedException;
import ccc.api.Failure;


/**
 * This exception indicates that a cycle was detected where a resource refers to
 * itself, either directly or indirectly.
 * <p>Examples would be an alias whose target points to itself or a folder that
 * contains itself.
 *
 * @author Civic Computing Ltd.
 */
public class CycleDetectedException
    extends
        RemoteExceptionSupport {

    /** {@inheritDoc} */
    @Override
    public CommandFailedException toRemoteException() {
        return new CommandFailedException(
            Failure.CYCLE, getUUID().toString());
    }
}