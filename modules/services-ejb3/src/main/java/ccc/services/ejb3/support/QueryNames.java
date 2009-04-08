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
package ccc.services.ejb3.support;


/**
 * Constants representing CCC queries.
 *
 * @author Civic Computing Ltd.
 */
public final class QueryNames {

    private QueryNames() { super(); }

    /** PENDING : String. */
    public static final String PENDING = "pending";

    /** EXECUTED : String. */
    public static final String EXECUTED = "executed";

    /** LOG_ENTRY_BY_ID : String. */
    public static final String LOG_ENTRY_BY_ID = "logEntryById";

    /** ALL_IMAGES : String. */
    public static final String ALL_IMAGES = "allImages";

    /** ROOT_BY_NAME : String. */
    public static final String ROOT_BY_NAME = "rootByName";

    /** RESOURCE_BY_LEGACY_ID : String. */
    public static final String RESOURCE_BY_LEGACY_ID = "resourceByLegacyId";

    /** ALL_PAGES : String. */
    public static final String ALL_PAGES = "allPages";

    /** ALL_FILES : String. */
    public static final String ALL_FILES = "allFiles";
}
