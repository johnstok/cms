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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ccc.api.Paragraph;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageRevision
    extends
        Revision implements IPage {

    private Set<Paragraph> _content = new HashSet<Paragraph>();

    /** Constructor: for persistence only. */
    protected PageRevision() { super(); }

    /**
     * Constructor.
     *
     * @param majorChange
     * @param comment
     * @param content
     */
    PageRevision(final Date timestamp,
                 final User actor,
                 final boolean majorChange,
                 final String comment,
                 final Set<Paragraph> content) {
        super(timestamp, actor, majorChange, comment);
        _content = content;
    }


    /** {@inheritDoc} */
    public final Set<Paragraph> getContent() {
        return _content;
    }
}
