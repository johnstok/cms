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

import static java.util.Collections.*;

import java.util.Map;
import java.util.TreeMap;

import ccc.commons.jee.DBC;


/**
 * A content resource.
 *
 * @author Civic Computing Ltd
 */
public final class Content extends Resource {

    private Map<String, Paragraph> content = new TreeMap<String, Paragraph>();

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    private Content() { super(); }

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     */
    public Content(final ResourceName name) {
        super(name);
    }

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param title The title of the resource.
     */
    public Content(final ResourceName name, final String title) {
        super(name, title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType type() {
        return ResourceType.CONTENT;
    }

    /**
     * Add a new paragraph for this content.
     *
     * @param key A unique string that identifies this paragraph.
     * @param paragraph The paragraph to be added.
     * @return 'this' - useful for method chaining.
     */
    public Content addParagraph(final String key, final Paragraph paragraph) {
        DBC.require().notEmpty(key);
        DBC.require().notNull(paragraph);
        content.put(key, paragraph);
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
