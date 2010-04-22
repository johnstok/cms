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
package ccc.api.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable2;


/**
 * A new page.
 *
 * @author Civic Computing Ltd.
 */
public class PageDto
    extends
        ResourceSnapshot
    implements
        Jsonable2 {

    private String    _comment;
    private boolean   _majorChange;
    private HashSet<Paragraph> _paragraphs = new HashSet<Paragraph>();


    /**
     * Constructor.
     */
    public PageDto() { super(); }


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
    public PageDto(final UUID parentId,
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
    public final boolean getMajorChange() {
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


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, getParent());
        json.set(JsonKeys.PARAGRAPHS, getParagraphs());
        json.set(
            JsonKeys.NAME, (null==getName()) ? null : getName().toString());
        json.set(JsonKeys.TEMPLATE_ID, getTemplate());
        json.set(JsonKeys.TITLE, getTitle());
        json.set(JsonKeys.COMMENT, _comment);
        json.set(JsonKeys.MAJOR_CHANGE, Boolean.valueOf(_majorChange));
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        setParent(json.getId(JsonKeys.PARENT_ID));
        for (final Json jsonPara : json.getCollection(JsonKeys.PARAGRAPHS)) {
            _paragraphs.add(new Paragraph(jsonPara));
        }
        final String name = json.getString(JsonKeys.NAME);
        setName((null==name) ? null : new ResourceName(name));
        setTemplate(json.getId(JsonKeys.TEMPLATE_ID));
        setTitle(json.getString(JsonKeys.TITLE));
        _comment = json.getString(JsonKeys.COMMENT);
        _majorChange = json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue();
    }

    public static PageDto delta(final Set<Paragraph> paragraphs) {
        final PageDto p = new PageDto();
        p.setParagraphs(paragraphs);
        return p;
    }
}
