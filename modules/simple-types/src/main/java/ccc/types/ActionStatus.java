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
package ccc.types;

/**
 * Supported statuses for an action.
 *
 * @author Civic Computing Ltd.
 */
public enum ActionStatus { // FIXME: Capitalise these enums?
    /** Scheduled : ActionStatus. */
    Scheduled,
    /** Complete : ActionStatus. */
    Complete,
    /** Failed : ActionStatus. */
    Failed,
    /** Cancelled : ActionStatus. */
    Cancelled;
}
