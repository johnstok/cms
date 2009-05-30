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

import ccc.api.DBC;
import ccc.api.Json;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.api.ResourceType;


/**
 * A page resource.
 *
 * @author Civic Computing Ltd
 */
public final class Page
    extends
        Resource
    implements
        WCAware<PageDelta> {

    private Set<Paragraph> _content = new HashSet<Paragraph>();
    private PageDelta      _workingCopy;

    /** MAXIMUM_PARAGRAPHS : int. */
    public static final int MAXIMUM_PARAGRAPHS = 32;


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
        DBC.require().maxValue(_content.size()+1, MAXIMUM_PARAGRAPHS);
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



    /* ====================================================================
     * Working copy implementation.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public void applySnapshot() {
        DBC.require().notNull(_workingCopy);
        final PageHelper pageHelper = new PageHelper();

        title(_workingCopy.getTitle());
        pageHelper.assignParagraphs(this, _workingCopy);

        clearWorkingCopy();

        final Template template = computeTemplate(null);
        if (null!=template) {
            pageHelper.validateFieldsForPage(
                paragraphs(), template.definition());
        }
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta workingCopy() {
        if (null!=_workingCopy) {
            return _workingCopy;
        }
        return createSnapshot();
    }

    /** {@inheritDoc} */
    @Override
    public void workingCopy(final Json snapshot) {
        workingCopy(new PageDelta(snapshot));
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta createSnapshot() {
        return new PageDelta(title(), new HashSet<Paragraph>(paragraphs()));
    }

    /** {@inheritDoc} */
    public void clearWorkingCopy() {
        DBC.require().notNull(_workingCopy);
        _workingCopy = null;
    }

    /** {@inheritDoc} */
    public void workingCopy(final PageDelta snapshot) {
        DBC.require().notNull(snapshot);
        _workingCopy = snapshot;
    }

    /** {@inheritDoc} */
    public boolean hasWorkingCopy() {
        return null!=_workingCopy;
    }

    @SuppressWarnings("unused")
    private String getWorkingCopyString() {
        if (null==_workingCopy) {
            return null;
        }
        final Snapshot s = new Snapshot();
        _workingCopy.toJson(s);
        return s.getDetail();
    }

    @SuppressWarnings("unused")
    private void setWorkingCopyString(final String wcs) {
        if (null==wcs) {
            return;
        }
        final Snapshot s = new Snapshot(wcs);
        _workingCopy = new PageDelta(s);
    }
}
