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
public final class Content extends Resource {

    private String name = id.toString();
    private ResourceName url = ResourceName.escape(name);
    private final Map<String, Paragraph> content =
        new HashMap<String, Paragraph>();

    /**
     * Constructor.
     *
     * @param name The name by which a user refers to this resource.
     */
    public Content(final String name) {
        this.name = name;
        url = ResourceName.escape(name);
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
     * @return The URL for this resource, as a {@link ResourceName}.
     */
    public ResourceName url() {
        return url;
    }

    /**
     * Accessor for the name field.
     *
     * @return The content's name, as a string.
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
    public Content addParagraph(final String key, final Paragraph paragraph) {
        content.put(key, paragraph); // TODO: validate parameters
        return this;
    }

    /**
     * Accessor for paragraphs.
     *
     * @return A map from unique key to the corresponding paragraph data.
     */
    public Map<String, Paragraph> paragraphs() {
        return content; // TODO: make a defensive copy
    }

    /**
     * Remove an existing paragraph.
     *
     * @param paragraphKey The key identifying the paragraph to be deleted.
     */
    public void deleteParagraph(final String paragraphKey) {
        content.remove(paragraphKey); // TODO: validate parameters
    }
}
