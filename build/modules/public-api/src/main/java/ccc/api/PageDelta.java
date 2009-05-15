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
import java.util.ArrayList;
import java.util.List;



/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class PageDelta implements Serializable {
    private String _title;
    private List<ParagraphDelta> _paragraphs = new ArrayList<ParagraphDelta>();

    @SuppressWarnings("unused") private PageDelta() { super(); }

    /**
     * Constructor.
     *
     * @param title
     * @param paragraphs
     */
    public PageDelta(final String title,
                     final List<ParagraphDelta> paragraphs) {
        _title = title;
        _paragraphs = paragraphs;
    }


    /**
     * Accessor.
     *
     * @return Returns the paragraphs.
     */
    public List<ParagraphDelta> getParagraphs() {
        return _paragraphs;
    }


    /**
     * Mutator.
     *
     * @param paragraphs The paragraphs to set.
     */
    public void setParagraphs(final List<ParagraphDelta> paragraphs) {
        _paragraphs = paragraphs;
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
}
