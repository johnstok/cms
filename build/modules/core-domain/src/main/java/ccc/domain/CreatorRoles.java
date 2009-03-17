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
package ccc.domain;


/**
 * Security roles for content creation.
 *
 * @author Civic Computing Ltd.
 */
public final class CreatorRoles {

    private CreatorRoles() { super(); }

    /** CONTENT_CREATOR : String. */
    public static final String CONTENT_CREATOR = "content_creator";

    /** SITE_BUILDER : String. */
    public static final String SITE_BUILDER = "site_builder";

    /** ADMINISTRATOR : String. */
    public static final String ADMINISTRATOR = "administrator";
}
