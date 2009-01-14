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

import java.io.IOException;
import java.io.OutputStream;

import ccc.domain.Resource;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface Body {

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    Resource getResource();

    /**
     * TODO: Add a description of this method.
     *
     * @param os
     */
    void write(OutputStream os) throws IOException;
}
