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
package ccc.rendering;

import java.util.UUID;

import ccc.entities.IResource;




/**
 * Reader API to lookup resources and navigate their associations, out with a
 * transaction.
 *
 * @author Civic Computing Ltd.
 */
public interface StatefulReader {

    /**
     * Look up a resource from its absolute path.
     *
     * @param absolutePath The absolute path to the resource.
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    IResource resourceFromPath(String absolutePath);

    /**
     * Look up a resource from its UUID.
     *
     * @param id The id of the resource.
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    IResource resourceFromId(String id);

    /**
     * Look up the contents of a file as a String.
     *
     * @param absolutePath The absolute path to the resource.
     * @param charset The character set for the file.
     *
     * @return The contents as a string.
     */
    String fileContentsFromPath(String absolutePath, String charset);


    /**
     * Create UUID from a String.
     *
     * @param id The id as a string.
     * @return The UUID.
     */
    UUID uuidFromString(String id);
}
