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

import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphSerializer;
import ccc.api.types.ResourceName;
import ccc.api.types.URIBuilder;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


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
        super.toJson(json);

        json.setJsons(
            JsonKeys.PARAGRAPHS,
            new ParagraphSerializer().write(json, getParagraphs()));
        json.set(JsonKeys.COMMENT, _comment);
        json.set(JsonKeys.MAJOR_CHANGE, Boolean.valueOf(_majorChange));
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        super.fromJson(json);

        for (final Json jsonPara : json.getCollection(JsonKeys.PARAGRAPHS)) {
            _paragraphs.add(new ParagraphSerializer().read(jsonPara));
        }
        _comment = json.getString(JsonKeys.COMMENT);
        _majorChange = json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue();
    }

    public static Page delta(final Set<Paragraph> paragraphs) {
        final Page p = new Page();
        p.setParagraphs(paragraphs);
        return p;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public static String list() { return ccc.api.core.ResourceIdentifiers.Page.COLLECTION; }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String validate() {
        return ccc.api.core.ResourceIdentifiers.Page.VALIDATOR;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String workingCopy() {
        return
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Page.WC)
            .replace("id", getId().toString())
            .toString();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String self() {
        return
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Page.ELEMENT)
            .replace("id", getId().toString())
            .toString();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param id
     * @return
     */
    public static String deltaURI(final UUID id) {
        return
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Page.DELTA)
            .replace("id", id.toString())
            .toString();
    }
}
