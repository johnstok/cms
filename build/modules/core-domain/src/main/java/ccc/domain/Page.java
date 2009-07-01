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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccc.api.DBC;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.api.ParagraphType;
import ccc.api.ResourceType;
import ccc.commons.WordCharFixer;
import ccc.snapshots.PageSnapshot;


/**
 * A page resource.
 *
 * @author Civic Computing Ltd.
 */
public class Page
    extends
        WorkingCopySupport<PageDelta, PageRevision> {

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
    Page(final String title) {
        super(title);
        update(
            new PageDelta(
                new HashSet<Paragraph>()),
            new RevisionMetadata(
                new Date(),
                User.SYSTEM_USER,
                true,
                "Created."));
    }

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param title The title of the resource.
     */
    Page(final ResourceName name, final String title) {
        super(name, title);
        update(
            new PageDelta(
                new HashSet<Paragraph>()),
            new RevisionMetadata(
                new Date(),
                User.SYSTEM_USER,
                true,
                "Created."));
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
                final RevisionMetadata metadata,
                final Paragraph... paragraphs) {
        super(name, title);
        template(template);
        update(
            new PageDelta(
                new HashSet<Paragraph>(Arrays.asList(paragraphs))),
                metadata);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType type() {
        return ResourceType.PAGE;
    }

    /**
     * @see IPage#paragraphs().
     */
    public Set<Paragraph> paragraphs() {
        return currentRevision().paragraphs();
    }

    /**
     * @see IPage#paragraph(String).
     */
    public Paragraph paragraph(final String name) {
        return currentRevision().paragraph(name);
    }

    /* ====================================================================
     * Working copy implementation.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public void applySnapshot(final RevisionMetadata metadata) {
        DBC.require().notNull(wc());

        update(wc().delta(), metadata);

        clearWorkingCopy();
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
    public PageDelta createSnapshot() {
        return new PageDelta(
            new HashSet<Paragraph>(currentRevision().paragraphs()));
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


    /** {@inheritDoc} */
    @Override
    public void setWorkingCopyFromRevision(final int revisionNumber) {
        final PageRevision rev = revision(revisionNumber);
        final PageDelta delta = new PageDelta(rev.getContent());
        workingCopy(delta);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param delta
     */
    public void update(final PageDelta delta,
                       final RevisionMetadata metadata) {
        DBC.require().maxValue(
            delta.getParagraphs().size(),
            MAXIMUM_PARAGRAPHS);

        final HashSet<Paragraph> paras = cleanParagraphs(delta);

        validateParagraphs(paras);

        addRevision(
            new PageRevision(
                metadata.getTimestamp(),
                metadata.getActor(),
                metadata.isMajorChange(),
                metadata.getComment(),
                paras));
    }

    private void validateParagraphs(final HashSet<Paragraph> paras) {

        final Template template = computeTemplate(null);
        if (null != template) {
            final PageHelper pageHelper = new PageHelper();
            pageHelper.validateFieldsForPage(paras, template.definition());
        }
    }

    private HashSet<Paragraph> cleanParagraphs(final PageDelta delta) {

        final HashSet<Paragraph> paras = new HashSet<Paragraph>();
        for (final Paragraph para : delta.getParagraphs()) {
            if (ParagraphType.TEXT == para.type()) {
                final WordCharFixer fixer = new WordCharFixer();
                final Paragraph p =
                    Paragraph.fromText(para.name(), fixer.fix(para.text()));
                paras.add(p);
            } else {
                paras.add(para);
            }
        }
        return paras;
    }



    /* ====================================================================
     * Snapshot support.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public final PageSnapshot forWorkingCopy() {
        return new PageSnapshot(this, wc());
    }

    /** {@inheritDoc} */
    @Override
    public final PageSnapshot forCurrentRevision() {
        return new PageSnapshot(this, currentRevision());
    }

    /** {@inheritDoc} */
    @Override
    public final PageSnapshot forSpecificRevision(final int revNo) {
        return new PageSnapshot(this, revision(revNo));
    }
}
