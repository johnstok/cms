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

import ccc.api.Paragraph;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageRevision
    extends
        Revision {

    private Set<Paragraph> _content = new HashSet<Paragraph>();

    /** Constructor: for persistence only. */
    protected PageRevision() { super(); }

    /**
     * Constructor.
     *
     * @param index
     * @param majorChange
     * @param comment
     * @param content
     */
    PageRevision(final int index,
                 final boolean majorChange,
                 final String comment,
                 final Set<Paragraph> content) {
        super(index, majorChange, comment);
        _content = content;
    }


    /**
     * Accessor.
     *
     * @return Returns the content.
     */
    public final Set<Paragraph> getContent() {
        return _content;
    }
}
