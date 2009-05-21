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

import java.util.Date;



/**
 * A command that can be executed using a CCC action.
 *
 * @param <T> The return type of the command.
 *
 * @author Civic Computing Ltd.
 */
public interface Command<T> {

    /**
     * Execute the command.
     *
     * @param action The action that supplies the command parameters.
     * @param happenedOn When the command took place.
     * @return A command specific result of type T.
     * @throws RemoteExceptionSupport If the command fails.
     */
    T execute(Action action, Date happenedOn) throws RemoteExceptionSupport;
}
