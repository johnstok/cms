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
import ccc.services.StatefulReader;


/**
 * A response body.
 *
 * @author Civic Computing Ltd.
 */
public interface Body {

    /**
     * Accessor.
     *
     * @return The resource this body will wraps.
     */
    Resource getResource();

    /**
     * Write the body to an {@link OutputStream}.
     *
     * @param os The stream to which the body will be written.
     * @param reader A stateful reader, used during writer to look up other
     *      resources.
     * @throws IOException - if writing to the output stream fails.
     */
    void write(OutputStream os, StatefulReader reader) throws IOException;
}
