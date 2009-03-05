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
package ccc.services;


/**
 * Constants representing the names of services in CCC.
 *
 * @author Civic Computing Ltd.
 */
@Deprecated
public final class ServiceNames {

    private ServiceNames() { super(); }

    /** STATEFUL_READER : String. */
    public static final String STATEFUL_READER =
        "application-ear-7.0.0-SNAPSHOT/StatefulReader/local";

    /** DATA_MANAGER_LOCAL : String. */
    public static final String DATA_MANAGER_LOCAL =
        "application-ear-7.0.0-SNAPSHOT/DataManager/local";

    /** SEARCH_ENGINE_LOCAL : String. */
    public static final String SEARCH_ENGINE_LOCAL =
        "application-ear-7.0.0-SNAPSHOT/Search/local";

}
