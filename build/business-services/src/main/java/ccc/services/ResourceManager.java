/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.services;

import java.util.Map;
import java.util.UUID;

import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;


/**
 * Business methods that operate on resources.
 *
 * @author Civic Computing Ltd
 */
public interface ResourceManager {

    /**
     * Lookup a resource, given its absolute path.
     *
     * @param path The absolute path to the resource.
     * @return The resource.
     */
    Resource lookup(ResourcePath path);

    /**
     * Create a folder, based on the specified path.
     *
     * @param pathString The string representation of the path.
     */
    void createFolder(String pathString);

    /**
     * Create the root folder for content.
     */
    void createRoot();

    /**
     * Create a content, based on the specified path.
     *
     * @param pathString The string representation of the path.
     */
    void createContent(String pathString);

    /**
     * Create paragraphs for given content.
     *
     * @param pathString The string representation of the path.
     * @param paragraphs A map containing the paragraphs to create, and their
     *      respective keys.
     */
    void createParagraphsForContent(String pathString,
                                    Map<String, Paragraph> paragraphs);

    /**
     * Lookup a resource, given its id.
     *
     * @param id
     * @return The resource.
     */
    Resource lookup(UUID id);

    /**
     * Recreates content's paragraphs.
     *
     * @param id
     * @param title
     * @param paragraphs
     */
    void saveContent(String id, String title, Map<String, String> paragraphs);
}
