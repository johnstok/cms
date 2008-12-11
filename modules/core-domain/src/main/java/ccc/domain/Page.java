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

import java.util.HashSet;
import java.util.Set;

import ccc.commons.DBC;


/**
 * A page resource.
 *
 * @author Civic Computing Ltd
 */
public final class Page extends Resource {

    private Set<Paragraph> _content = new HashSet<Paragraph>();


    /** Constructor: for persistence only. */
    protected Page() { super(); }

    /**
     * Constructor.
     *
     * @param title The title for the resource.
     */
    public Page(final String title) {
        super(title);
    }

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param title The title of the resource.
     */
    public Page(final ResourceName name, final String title) {
        super(name, title);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType type() {
        return ResourceType.PAGE;
    }

    /**
     * Add a new paragraph for this content.
     *
     * @param paragraph The paragraph to be added.
     * @return 'this' - useful for method chaining.
     */
    public Page addParagraph(final Paragraph paragraph) {
        DBC.require().notNull(paragraph);
        _content.add(paragraph);
        return this;
    }

    /**
     * Accessor for paragraphs.
     *
     * @return A map from unique key to the corresponding paragraph data.
     */
    public Set<Paragraph> paragraphs() {
        return unmodifiableSet(_content);
    }

    /**
     * Remove an existing paragraph.
     *
     * @param paragraphKey The key identifying the paragraph to be deleted.
     */
    public void deleteParagraph(final String paragraphKey) {
        DBC.require().notEmpty(paragraphKey);
        _content.remove(paragraph(paragraphKey));
    }

    /**
     * Deletes all paragraphs.
     *
     */
    public void deleteAllParagraphs() {
        _content.clear();
    }

    /**
     * Look up a paragraph on this page by name.
     *
     * @param name The name of the paragraph to retrieve.
     * @return The paragraph with the specified name.
     */
    public Paragraph paragraph(final String name) {
        for (final Paragraph p : _content) {
            if (p.name().equals(name)) {
                return p;
            }
        }
        throw new CCCException("No paragraph with name: "+name);
    }
}
