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
package ccc.services.api;


/**
 * Constants representing the names of services in CCC.
 *
 * @author Civic Computing Ltd.
 */
@Deprecated
public final class ServiceNames {

    private ServiceNames() { super(); }

    /** PUBLIC_QUERIES : String. */
    public static final String PUBLIC_QUERIES =
        "application-ear-7.0.0-SNAPSHOT/PublicQueries/remote";

    /** PUBLIC_COMMANDS : String. */
    public static final String PUBLIC_COMMANDS =
        "application-ear-7.0.0-SNAPSHOT/PublicCommands/remote";

    /** PUBLIC_COMMANDS : String. */
    public static final String PUBLIC_SCHEDULER =
        "application-ear-7.0.0-SNAPSHOT/Scheduler/remote";
}
