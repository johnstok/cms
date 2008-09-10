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

import ccc.commons.Maybe;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.Template;


/**
 * Business methods that operate on content.
 *
 * @author Civic Computing Ltd
 */
public interface ContentManager {

    /**
     * Lookup a resource, given its absolute path.
     *
     * @param path The absolute path to the resource.
     * @param <T> The type of the resource to look up.
     * @return The resource.
     */
    <T extends Resource> Maybe<T> lookup(ResourcePath path);

    /**
     * Lookup a resource, given its id.
     *
     * @param id The unique identifier for the resource to look up.
     * @param <T> The type of the resource to look up.
     * @return The resource.
     */
    <T extends Resource> T lookup(UUID id);

    /**
     * Look up the root folder for content.
     *
     * @return The root folder for content.
     */
    Folder lookupRoot();

    /**
     * Create the root folder for content.
     */
    void createRoot();

    /**
     * Create a folder, based on the specified path.
     *
     * @param folderId The {@link UUID} for the containing folder/
     * @param newFolder The folder to be created.
     * @return The newly created folder.
     */
    Folder create(UUID folderId, Folder newFolder);

    /**
     * Create a page, based on the specified path.
     *
     * @param folderId The {@link UUID} for the containing folder/
     * @param newPage The page to be created.
     */
    void create(UUID folderId, Page newPage);

    /**
     * Recreates a page's paragraphs.
     *
     * @param id The unique identifier for the page.
     * @param newTitle The new title for the page.
     * @param newParagraphs The new paragraphs for the page. All existing
     *      paragraphs will be removed and replaced with the paragraphs
     *      specified here.
     */
    void update(UUID id, String newTitle, Map<String, String> newParagraphs);

    /**
     * Sets the new default template for content.
     *
     * @param newDefault The template to set as default.
     */
    void setDefaultTemplate(Template newDefault);

    /**
     * For migration testing. Lookup a Page, given its path.
     * Initialises paragraph collection and template.
     *
     * @param path The absolute path to the resource.
     * @return The resource.
     */
    Page eagerPageLookup(ResourcePath path);
}
