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
public final class ServiceNames {

    private ServiceNames() { super(); }

    /** CONTENT_MANAGER_LOCAL : String. */
    public static final String CONTENT_MANAGER_LOCAL =
        "application-ear-7.0.0-SNAPSHOT/ContentManager/local";

    /** ASSET_MANAGER_LOCAL : String. */
    public static final String ASSET_MANAGER_LOCAL =
        "application-ear-7.0.0-SNAPSHOT/AssetManager/local";

    /** DATA_MANAGER_LOCAL : String. */
    public static final String DATA_MANAGER_LOCAL =
        "application-ear-7.0.0-SNAPSHOT/DataManager/local";

    /** USER_MANAGER_LOCAL : String. */
    public static final String USER_MANAGER_LOCAL =
        "application-ear-7.0.0-SNAPSHOT/UserManager/local";

    /** CONTENT_MANAGER_REMOTE : String. */
    public static final String CONTENT_MANAGER_REMOTE =
        "application-ear-7.0.0-SNAPSHOT/ContentManager/remote";

    /** ASSET_MANAGER_REMOTE : String. */
    public static final String ASSET_MANAGER_REMOTE =
        "application-ear-7.0.0-SNAPSHOT/AssetManager/remote";

    /** USER_MANAGER_REMOTE : String. */
    public static final String USER_MANAGER_REMOTE =
        "application-ear-7.0.0-SNAPSHOT/UserManager/remote";

    /** RESOURCE_DAO_LOCAL : String. */
    public static final String RESOURCE_DAO_LOCAL =
        "application-ear-7.0.0-SNAPSHOT/ResourceDAO/local";
}
