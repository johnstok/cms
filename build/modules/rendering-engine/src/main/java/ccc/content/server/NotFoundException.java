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
package ccc.content.server;


/**
 * An exception to represent a missing resource.
 *
 * @author Civic Computing Ltd.
 */
public class NotFoundException
    extends
        RuntimeException {

    /**
     * Constructor.
     */
    NotFoundException() { super("Resource not found"); }
}
