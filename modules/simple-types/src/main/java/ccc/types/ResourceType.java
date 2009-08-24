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

package ccc.types;

/**
 * An enumeration of the valid types of a resource.
 *
 * @author Civic Computing Ltd
 */
public enum ResourceType {

    /** PAGE : ResourceType. */
    PAGE,

    /** FOLDER : ResourceType. */
    FOLDER,

    /** TEMPLATE : ResourceType. */
    TEMPLATE,

    /** FILE : ResourceType. */
    FILE,

    /** ALIAS : ResourceType. */
    ALIAS,

    /** SEARCH : ResourceType. */
    SEARCH;
}
