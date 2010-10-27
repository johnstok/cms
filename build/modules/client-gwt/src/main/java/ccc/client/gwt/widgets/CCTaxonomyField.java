/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Revision      $Rev: 3145 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-09-24 12:09:46 +0100 (Fri, 24 Sep 2010) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import java.util.List;

import ccc.api.core.ResourceSummary;
import ccc.api.types.Paragraph;
import ccc.client.widgets.PageElement;


/**
 * A taxonomy element on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCTaxonomyField
    extends
        PageElement<TaxonomyTriggerField> {

    private final TaxonomyTriggerField _uiControl;
    private final String _vocabularyID;


    /**
     * Constructor.
     *
     * @param name          The field's name.
     * @param title         The field's title.
     * @param desc          The field's description.
     * @param vocabularyID  The  vocabulary id.
     * @param targetRoot The root resource containing resources.
     */
    public CCTaxonomyField(final String name,
                           final String title,
                           final String desc,
                           final String vocabularyID,
                           final ResourceSummary targetRoot) {
        super(name);
        _vocabularyID = vocabularyID;
        _uiControl = new TaxonomyTriggerField(targetRoot, _vocabularyID);
        _uiControl.setFieldLabel(createLabel(name, title));
        _uiControl.setToolTip(createTooltip(name, title, desc));
    }


    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final List<String> terms = _uiControl.getTerms();
        return Paragraph.fromTaxonomy(getName(), terms);
    }


    /** {@inheritDoc} */
    @Override
    public TaxonomyTriggerField getUI() { return _uiControl; }


    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {
        final List<String> text = para.getList();
        _uiControl.setTerms(text);
    }
}
