/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.core;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.Link;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;


/**
 * A new page.
 *
 * @author Civic Computing Ltd.
 */
public class Page
    extends
        Resource {

    private String    _comment;
    private boolean   _majorChange;
    private HashSet<Paragraph> _paragraphs = new HashSet<Paragraph>();


    /**
     * Constructor.
     */
    public Page() { super(); }


    /**
     * Constructor.
     *
     * @param parentId The page's parent folder id.
     * @param name The page's name.
     * @param templateId The page's template id.
     * @param title The page's title.
     * @param comment The comment for a page update.
     * @param majorChange Is the update a major change.
     */
    @Deprecated
    public Page(final UUID parentId,
                final String name,
                final UUID templateId,
                final String title,
                final String comment,
                final boolean majorChange) {
        setParent(parentId);
        setName(new ResourceName(name));
        setTemplate(templateId);
        setTitle(title);
        _comment = comment;
        _majorChange = majorChange;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public final String getComment() {
        return _comment;
    }

    /**
     * Accessor.
     *
     * @return Returns the majorChange.
     */
    public final boolean isMajorChange() {
        return _majorChange;
    }


    /**
     * Mutator.
     *
     * @param comment The comment to set.
     */
    public void setComment(final String comment) {
        _comment = comment;
    }


    /**
     * Mutator.
     *
     * @param majorChange The majorChange to set.
     */
    public void setMajorChange(final boolean majorChange) {
        _majorChange = majorChange;
    }


    /**
     * Accessor.
     *
     * @return Returns the paragraphs.
     */
    public Set<Paragraph> getParagraphs() {
        return _paragraphs;
    }


    /**
     * Mutator.
     *
     * @param paragraphs The paragraphs to set.
     */
    public void setParagraphs(final Set<Paragraph> paragraphs) {
        _paragraphs = new HashSet<Paragraph>(paragraphs);
    }


    /**
     * Look up a paragraph on this page by name.
     *
     * @param name The name of the paragraph to retrieve.
     * @return The paragraph with the specified name.
     */
    public Paragraph getParagraph(final String name) {
        for (final Paragraph p : _paragraphs) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }


    /**
     * Factory method - create a page for a set of paragraphs.
     *
     * @param paragraphs The paragraphs for the page.
     *
     * @return A new page containing the specified paragraphs.
     */
    @Deprecated
    public static Page delta(final Set<Paragraph> paragraphs) {
        final Page p = new Page();
        p.setParagraphs(paragraphs);
        return p;
    }


    /**
     * Link.
     *
     * @return The link to this Page's working copy.
     */
    public Link workingCopy() {
        return new Link(getLink(WORKING_COPY));
    }


    /** WORKING_COPY : String. */
    public static final String WORKING_COPY = "wc";
    /** VALIDATOR : String. */
    public static final String VALIDATOR = "page-validator";
}
