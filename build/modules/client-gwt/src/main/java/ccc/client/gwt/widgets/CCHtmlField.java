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
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import ccc.api.types.Paragraph;
import ccc.client.core.I18n;
import ccc.client.core.ValidationResult;


/**
 * A html editor on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCHtmlField
    extends
        PageElement<FCKEditor> {

    // TODO: Can we extend CCTextField?

    private FCKEditor _editor;


    /**
     * Constructor.
     *
     * @param name   The field's name.
     * @param title  The field's title.
     * @param desc   The field's description.
     */
    public CCHtmlField(final String name,
                       final String title,
                       final String desc) {
        super(name);

        final FCKEditor fck =
            new FCKEditor(
                "",
                "250px",
                createTooltip(name, title, desc),
                createLabel(name, title));
        _editor = fck;
    }


    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final FCKEditor f = _editor;
        final Paragraph p =
            Paragraph.fromText(getName(), f.getHTML());
        return p;
    }


    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {
        final FCKEditor fck =
            new FCKEditor(
                para.getText(),
                "250px",
                _editor.getToolTip2(),
                _editor.getLabel());
        _editor = fck;
    }


    /** {@inheritDoc} */
    @Override
    public FCKEditor getUI() { return _editor; }


    /** {@inheritDoc} */
    @Override
    public void validate(final ValidationResult vResult) {
        final String fieldName = getName();
        final String fieldText = _editor.getHTML();
        if (null!= fieldText
            && fieldText.length() > Paragraph.MAX_TEXT_LENGTH) {
            vResult.addError(I18n.UI_MESSAGES.paragraphTooLarge(fieldName));
        }
    }
}
