/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import java.util.HashSet;
import java.util.Set;

import ccc.entities.IPage;
import ccc.rest.dto.PageDelta;
import ccc.types.Paragraph;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageWorkingCopy
    extends
        WorkingCopy<PageDelta>
    implements
        IPage {

    private Set<Paragraph>     _wcContent;

    /** Constructor: for persistence only. */
    protected PageWorkingCopy() { super(); }

    /**
     * Constructor.
     *
     * @param snapshot
     */
    public PageWorkingCopy(final PageDelta snapshot) {
        delta(snapshot);
    }

    /** {@inheritDoc} */
    @Override
    protected PageDelta delta() {
        return new PageDelta(_wcContent);
    }

    /** {@inheritDoc} */
    @Override
    public void delta(final PageDelta snapshot) {
        _wcContent = new HashSet<Paragraph>(snapshot.getParagraphs());
    }

    /** {@inheritDoc} */
    @Override
    public Set<Paragraph> getContent() {
        return new HashSet<Paragraph>(_wcContent);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Paragraph> paragraphs() { // TODO: Duplicated in PageRevision
        return unmodifiableSet(_wcContent);
    }


    /** {@inheritDoc} */
    @Override
    // TODO: Duplicated in PageRevision
    public Paragraph paragraph(final String name) {
        for (final Paragraph p : _wcContent) {
            if (p.name().equals(name)) {
                return p;
            }
        }
        throw new CCCException("No paragraph with name: "+name);
    }
}
