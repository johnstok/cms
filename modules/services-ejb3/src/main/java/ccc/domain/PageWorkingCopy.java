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

import java.util.HashSet;
import java.util.Set;

import ccc.api.dto.PageDelta;
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
            if (p.getName().equals(name)) {
                return p;
            }
        }
        throw new RuntimeException("No paragraph with name: "+name);
    }
}
