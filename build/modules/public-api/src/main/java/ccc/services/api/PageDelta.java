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
package ccc.services.api;

import java.util.ArrayList;
import java.util.List;



/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class PageDelta
    extends
        ResourceDelta {
    private List<ParagraphDelta> _paragraphs = new ArrayList<ParagraphDelta>();
    private TemplateDelta _computedTemplate;

    @SuppressWarnings("unused") private PageDelta() { super(); }

    /**
     * Constructor.
     *
     * @param id
     * @param name
     * @param title
     * @param templateId
     * @param tags
     * @param published
     * @param paragraphs
     * @param computedTemplate
     */
    public PageDelta(final ID id,
                     final String name,
                     final String title,
                     final ID templateId,
                     final String tags,
                     final boolean published,
                     final List<ParagraphDelta> paragraphs,
                     final TemplateDelta computedTemplate) {

        super(id, name, title, templateId, tags, published);
        _paragraphs = paragraphs;
        _computedTemplate = computedTemplate;
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
     * @return Returns the computedTemplate.
     */
    public TemplateDelta getComputedTemplate() {
        return _computedTemplate;
    }


    /**
     * Mutator.
     *
     * @param computedTemplate The computedTemplate to set.
     */
    public void setComputedTemplate(final TemplateDelta computedTemplate) {
        _computedTemplate = computedTemplate;
    }
}
