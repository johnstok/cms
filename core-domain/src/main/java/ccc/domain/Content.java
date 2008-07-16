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

package ccc.domain;

import java.util.HashMap;
import java.util.Map;


/**
 * A content resource.
 * 
 * @author Civic Computing Ltd
 */
public class Content extends Resource {

    private String       name                = id.toString();
    private ResourceName url                 = ResourceName.escape(name);
    private Map<String, Paragraph> content   = new HashMap<String, Paragraph>();

    /**
     * Constructor.
     * 
     * @param content
     */
    public Content(String name) {
        this.name = name;
        this.url = ResourceName.escape(name);
    }

    /**
     * @see ccc.domain.Resource#type()
     */
    @Override
    public ResourceType type() {
        return ResourceType.CONTENT;
    }

    /**
     * Accessor for URL.
     * 
     * @return
     */
    public ResourceName url() {
        return url;
    }

    /**
     * Accessor for the name field.
     * 
     * @return
     */
    public String name() {
        return name;
    }

    /**
     * Add a new paragraph for this content.
     * 
     * @param key
     * @param paragraph
     */
    public Content addParagraph(String key, Paragraph paragraph) {
        content.put(key, paragraph); // TODO: validate parameters
        return this;
    }

    /**
     * Accessor for paragraphs.
     * 
     * @return
     */
    public Map<String, Paragraph> paragraphs() {
        return content; // TODO: make a defensive copy
    }

    /**
     * Remove an existing paragraph.
     *
     * @param string
     */
    public void deleteParagraph(String paragraphKey) {
        content.remove(paragraphKey); // TODO: validate parameters
    }
}
