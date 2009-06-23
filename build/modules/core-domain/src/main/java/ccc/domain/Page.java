/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccc.api.DBC;
import ccc.api.Json;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.api.ResourceType;


/**
 * A page resource.
 *
 * @author Civic Computing Ltd.
 */
public final class Page
    extends
        Resource
    implements
        WCAware<PageDelta> {

    private List<PageRevision> _history = new ArrayList<PageRevision>();
    private int                _pageVersion = -1;

    // This is a collection to exploit hibernate's delete-orphan syntax.
    private List<PageWorkingCopy> _workingCopies =
        new ArrayList<PageWorkingCopy>();

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
        _pageVersion++;
        _history.add(
            new PageRevision(
                _pageVersion, true, "Created.", new HashSet<Paragraph>()));
    }

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param title The title of the resource.
     */
    public Page(final ResourceName name, final String title) {
        super(name, title);
        _pageVersion++;
        _history.add(
            new PageRevision(
                _pageVersion, true, "Created.", new HashSet<Paragraph>()));
    }

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param title The title of the resource.
     */
    public Page(final ResourceName name,
                final String title,
                final Template template,
                final Paragraph... paragraphs) {
        super(name, title);
        template(template);
        workingCopy(
            new PageDelta(
                title, new HashSet<Paragraph>(Arrays.asList(paragraphs))));
        applySnapshot();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType type() {
        return ResourceType.PAGE;
    }

    /**
     * Accessor for paragraphs.
     *
     * @return A map from unique key to the corresponding paragraph data.
     */
    public Set<Paragraph> paragraphs() {
        return unmodifiableSet(currentRevision().getContent());
    }

    /**
     * Look up a paragraph on this page by name.
     *
     * @param name The name of the paragraph to retrieve.
     * @return The paragraph with the specified name.
     */
    public Paragraph paragraph(final String name) {
        for (final Paragraph p : currentRevision().getContent()) {
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
        DBC.require().notNull(wc());
        DBC.require().maxValue(
            wc().delta().getParagraphs().size(),
            MAXIMUM_PARAGRAPHS);
        DBC.require().notNull(wc().delta().getTitle());
        final PageHelper pageHelper = new PageHelper();

        title(wc().delta().getTitle());
//        assignParagraphs(_workingCopy);

        final Template template = computeTemplate(null);
        if (null!=template) {
            pageHelper.validateFieldsForPage(
                paragraphs(), template.definition());
        }

        _pageVersion++;
        _history.add(
            new PageRevision(
                _pageVersion,
                true,
                "Updated.",
                new HashSet<Paragraph>(wc().delta().getParagraphs())));

        clearWorkingCopy();
    }

    // FIXME: Add char fixing.
    private void assignParagraphs(final PageDelta delta) {
//        for (final Paragraph para : delta.getParagraphs()) {
//
//            if (ParagraphType.TEXT == para.type()) {
//                final WordCharFixer fixer = new WordCharFixer();
//                final Paragraph p = Paragraph.fromText(para.name(), fixer.fix(para.text()));
//                addParagraph(p);
//            } else {
//                addParagraph(para);
//            }
//        }
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta workingCopy() {
        if (null!=wc()) {
            return wc().delta();
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
    public void workingCopy(final PageDelta snapshot) {
        DBC.require().notNull(snapshot);
        if (hasWorkingCopy()) {
            wc().delta(snapshot);
        } else {
            wc(new PageWorkingCopy(snapshot));
        }
    }

    //--

    /** {@inheritDoc} */
    public void clearWorkingCopy() {
        DBC.require().toBeTrue(hasWorkingCopy());
        _workingCopies.clear();
    }

    /** {@inheritDoc} */
    public boolean hasWorkingCopy() {
        return 0!=_workingCopies.size();
    }

    private PageWorkingCopy wc() {
        if (0==_workingCopies.size()) {
            return null;
        }
        return _workingCopies.get(0);
    }

    private void wc(final PageWorkingCopy pageWorkingCopy) {
        DBC.require().toBeFalse(hasWorkingCopy());
        _workingCopies.add(0, pageWorkingCopy);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public PageRevision currentRevision() {
        for (final PageRevision r : _history) {
            if (_pageVersion==r.getIndex()) {
                return r;
            }
        }
        throw new RuntimeException("No current revision!");
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param i
     * @return
     */
    public PageRevision revision(final int i) {
        for (final PageRevision r : _history) {
            if (i==r.getIndex()) {
                return r;
            }
        }
        throw new RuntimeException("No current revision!");
    }
}
