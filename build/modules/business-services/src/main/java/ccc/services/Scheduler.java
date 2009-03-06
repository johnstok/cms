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
package ccc.services;

import ccc.actions.Action;



/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface Scheduler {
    void start();
    void stop();
//    boolean isRunning();
    void schedule(Action action);
}
