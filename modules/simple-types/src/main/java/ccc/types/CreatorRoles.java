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
package ccc.types;


/**
 * Security roles for content creation.
 *
 * @author Civic Computing Ltd.
 */
public final class CreatorRoles {

    private CreatorRoles() { super(); }

    /** CONTENT_CREATOR : String. */
    public static final String CONTENT_CREATOR = "CONTENT_CREATOR";

    /** SITE_BUILDER : String. */
    public static final String SITE_BUILDER = "SITE_BUILDER";

    /** ADMINISTRATOR : String. */
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
}
