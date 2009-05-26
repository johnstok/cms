/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;



/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class PageDelta implements Serializable, Jsonable {
    private String _title;
    private HashSet<Paragraph> _paragraphs = new HashSet<Paragraph>();

    @SuppressWarnings("unused") private PageDelta() { super(); }

    /**
     * Constructor.
     *
     * @param title The page's title.
     * @param paragraphs The page's paragraphs.
     */
    public PageDelta(final String title,
                     final Set<Paragraph> paragraphs) {
        _title = title;
        _paragraphs = new HashSet<Paragraph>(paragraphs);
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of a page delta.
     */
    public PageDelta(final Json json) {
        setTitle(json.getString("title"));
        for (final Json jsonPara : json.getCollection("paragraphs")) {
            _paragraphs.add(new Paragraph(jsonPara));
        }
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
     * Accessor.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return _title;
    }


    /**
     * Mutator.
     *
     * @param title The title to set.
     */
    public void setTitle(final String title) {
        _title = title;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) { // TODO: Use JsonKeys
        json.set("title", getTitle());
        json.set("paragraphs", getParagraphs());
    }
}
