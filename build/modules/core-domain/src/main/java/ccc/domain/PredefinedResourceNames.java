/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

/**
 * A list of resource names for predefined resources.
 *
 * @author Civic Computing Ltd
 */
public final class PredefinedResourceNames {

    private PredefinedResourceNames() { /* NO-OP */ }

    /** CONTENT : ResourceName. */
    public static final ResourceName CONTENT = new ResourceName("content");

    /** ASSETS : ResourceName. */
    public static final ResourceName ASSETS  = new ResourceName("assets");
}
