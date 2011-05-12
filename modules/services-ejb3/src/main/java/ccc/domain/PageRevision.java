/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.api.core.Page;
import ccc.api.types.Paragraph;


/**
 * The contents of a page at a specific point in time.
 *
 * @author Civic Computing Ltd.
 */
public class PageRevision
    extends
        RevisionEntity<Page> {

    private Set<Paragraph> _content = new HashSet<Paragraph>();
    private PageEntity     _page    = null;
    private Integer        _revNo   = null;


    /** Constructor: for persistence only. */
    protected PageRevision() { super(); }


    /**
     * Constructor.
     *
     * @param timestamp The date the revision was created.
     * @param actor The actor that created the revision.
     * @param majorChange Was the revision a major change.
     * @param comment Comment describing the change.
     * @param content The new content for the page.
     */
    PageRevision(final Date timestamp,
                 final UserEntity actor,
                 final boolean majorChange,
                 final String comment,
                 final Set<Paragraph> content) {
        super(timestamp, actor, majorChange, comment);
        _content = content;
    }


    /** {@inheritDoc} */
    public Set<Paragraph> getParagraphs() {
        return unmodifiableSet(_content);
    }


    /** {@inheritDoc} */
    public Paragraph getParagraph(final String name) {
        for (final Paragraph p : _content) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        throw new RuntimeException("No paragraph with name: "+name);
    }


    /** {@inheritDoc} */
    @Override
    protected Page delta() {
        return Page.delta(_content);
    }
}
