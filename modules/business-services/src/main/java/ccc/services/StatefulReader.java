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
package ccc.services;

import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;


/**
 * Reader API to lookup resources and navigate their associations, out with a
 * transaction.
 *
 * @author Civic Computing Ltd.
 */
public interface StatefulReader {

    /** NAME : String. */
    String NAME = "StatefulReader";

    /**
     * Look up a resource from its absolute path.
     *
     * @param contentPath ResourcePath The path to the resource.
     * @param rootName The name of the root folder in which the resource exists.
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    Resource lookup(String rootName, ResourcePath contentPath);

    /**
     * Lookup a log entry.
     *
     * @param index The unique index of the log entry.
     * @return The log entry with the specified index.
     */
    LogEntry lookup(long index);

    /**
     * Determine the absolute path of a resource given its legacy id.
     *
     * @param legacyId The resource's id in CCC6.
     * @return The absolute path as a string.
     */
    String absolutePath(String legacyId);

    /**
     * Look up a resource from its UUID.
     *
     * @param id The id of the resource.
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    Resource lookup(String id);

}
