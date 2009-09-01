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

import java.util.Date;
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
public class PageRevision
    extends
        Revision<PageDelta>
    implements
        IPage {

    private Set<Paragraph> _content = new HashSet<Paragraph>();


    /** Constructor: for persistence only. */
    protected PageRevision() { super(); }


    /**
     * Constructor.
     *
     * @param timestamp
     * @param actor
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


    /** {@inheritDoc} */
    @Override
    public Set<Paragraph> paragraphs() {
        return unmodifiableSet(_content);
    }


    /** {@inheritDoc} */
    @Override
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
    protected PageDelta delta() {
        return new PageDelta(_content);
    }
}
