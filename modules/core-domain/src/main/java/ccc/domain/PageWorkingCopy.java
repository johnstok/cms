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

import java.util.HashSet;
import java.util.Set;

import ccc.api.PageDelta;
import ccc.api.Paragraph;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageWorkingCopy
    extends
        WorkingCopy
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

    PageDelta delta() {
        return new PageDelta(_wcContent);
    }

    public void delta(final PageDelta snapshot) {
        _wcContent = new HashSet<Paragraph>(snapshot.getParagraphs());
    }

    /** {@inheritDoc} */
    @Override
    public Set<Paragraph> getContent() {
        return new HashSet<Paragraph>(_wcContent);
    }
}
