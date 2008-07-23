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
     * @param pathString
     */
    void createFolder(String pathString);

    /**
     * Create the root folder for content.
     */
    void createRoot();
    
    /**
     * Create a content, based on the specified path.
     *
     * @param pathString
     */
    void createContent(String pathString);
    
    /**
     * Create paragraphs for given content
     *
     * @param content
     * @param paragraphs
     */
    void createParagraphsForContent(String pathString, Map<String, Paragraph> paragraphs);
}
