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
package ccc.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.Paragraph;



/**
 * A delta class, for updating a page.
 *
 * @author Civic Computing Ltd.
 */
public final class PageDelta implements Serializable, Jsonable {

    private HashSet<Paragraph> _paragraphs = new HashSet<Paragraph>();

    @SuppressWarnings("unused") private PageDelta() { super(); }

    /**
     * Constructor.
     *
     * @param paragraphs The page's paragraphs.
     */
    public PageDelta(final Set<Paragraph> paragraphs) {
        _paragraphs = new HashSet<Paragraph>(paragraphs);
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of a page delta.
     */
    public PageDelta(final Json json) {
        for (final Json jsonPara : json.getCollection(JsonKeys.PARAGRAPHS)) {
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


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARAGRAPHS, getParagraphs());
    }
}
