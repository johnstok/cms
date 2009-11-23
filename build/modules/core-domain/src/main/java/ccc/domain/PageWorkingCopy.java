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

import ccc.rest.dto.PageDelta;
import ccc.types.Paragraph;


/**
 * A working copy for a page.
 *
 * @author Civic Computing Ltd.
 */
public class PageWorkingCopy
    extends
        WorkingCopy<PageDelta> {

    private Set<Paragraph> _wcContent;

    /** Constructor: for persistence only. */
    protected PageWorkingCopy() { super(); }

    /**
     * Constructor.
     *
     * @param content The contents of this working copy.
     */
    public PageWorkingCopy(final PageDelta content) {
        delta(content);
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
    public Set<Paragraph> getParagraphs() { // TODO: Duplicated in PageRevision
        return unmodifiableSet(_wcContent);
    }


    /** {@inheritDoc} */
    // TODO: Duplicated in PageRevision
    public Paragraph getParagraph(final String name) {
        for (final Paragraph p : _wcContent) {
            if (p.name().equals(name)) {
                return p;
            }
        }
        throw new CCCException("No paragraph with name: "+name);
    }
}
