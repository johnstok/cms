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

import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

import ccc.commons.jee.DBC;


/**
 * A content resource.
 *
 * @author Civic Computing Ltd
 */
public final class Content extends Resource {

    /**
     * Constructor.
     *
     * @param name
     */
    public Content(final ResourceName name) {
        super(name);
    }

    /**
     * Constructor.
     *
     * @param name
     * @param title
     */
    public Content(final ResourceName name, final String title) {
        super(name, title);
    }

    private final Map<String, Paragraph> content =
        new HashMap<String, Paragraph>();

    /**
     * @see ccc.domain.Resource#type()
     */
    @Override
    public ResourceType type() {
        return ResourceType.CONTENT;
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
        return unmodifiableMap(content);
    }

    /**
     * Remove an existing paragraph.
     *
     * @param paragraphKey The key identifying the paragraph to be deleted.
     */
    public void deleteParagraph(final String paragraphKey) {
        DBC.require().notEmpty(paragraphKey);
        content.remove(paragraphKey);
    }
}
