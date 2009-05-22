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
package ccc.content.response;

import javax.servlet.http.HttpServletResponse;


/**
 * API for a ccc response header.
 *
 * @author Civic Computing Ltd.
 */
public interface Header {

    /**
     * Write this header to a servlet response.
     *
     * @param response The servlet response.
     */
    void writeTo(HttpServletResponse response);
}
