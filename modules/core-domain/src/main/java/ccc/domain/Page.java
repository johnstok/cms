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

import java.util.ArrayList;
import java.util.Collection;
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
    private Snapshot _workingCopy;


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

    /** {@inheritDoc} */
    @Override
    public Snapshot createSnapshot() {
        final Snapshot s = super.createSnapshot();
        final Collection<Snapshot> paras = new ArrayList<Snapshot>();
        for (final Paragraph p : _content) {
            paras.add(p.createSnapshot());
        }
        s.set("paragraphs", paras);
        return s;
    }

    /**
     * Apply a snapshot to this object. The title and paragraph fields will be
     * updated. Any other entries in the snapshot will be ignored.
     *
     * @param s The snapshot to apply.
     */
    public void applySnapshot(final Snapshot s) {
        title(s.getString("title"));
        deleteAllParagraphs();
        for(final Snapshot p : s.getSnapshots("paragraphs")) {
            addParagraph(Paragraph.fromSnapshot(p));
        }
    }

    /**
     * Create a new snapshot of this object and set it as the current working
     * copy.
     */
    public void createWorkingCopy() {
        DBC.require().toBeNull(_workingCopy);
        _workingCopy = createSnapshot();
    }

    /**
     * Accessor.
     *
     * @return The current working copy for this page, or null if there is no
     *      working copy.
     */
    public Snapshot workingCopy() {
        return _workingCopy;
    }

    /**
     * Clear the current working copy.
     */
    public void clearWorkingCopy() {
        DBC.require().notNull(_workingCopy);
        _workingCopy = null;
    }

    /**
     * Mutator.
     *
     * @param snapshot The new working copy for this page.
     */
    public void workingCopy(final Snapshot snapshot) {
        DBC.require().notNull(_workingCopy);
        _workingCopy = snapshot;
    }
}
