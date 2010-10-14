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
import ccc.client.widgets.PageElement;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * A text field on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCTextField
    extends
        PageElement<TextField<String>> {

    private TextField<String> _field;


    /**
     * Constructor.
     *
     * @param name   The field's name.
     * @param title  The field's title.
     * @param desc   The field's description.
     * @param regexp The regular expression validating the field.
     */
    public CCTextField(final String name,
                       final String title,
                       final String desc,
                       final String regexp) {
        super(name);

        final TextField<String> tf = createField();
        tf.setMaxLength(Paragraph.MAX_TEXT_LENGTH);

        tf.setFieldLabel(createLabel(name, title));
        tf.setToolTip(createTooltip(name, title, desc));
        if (regexp != null) {
            tf.setRegex(regexp);
        }
        _field = tf;
    }


    /**
     * Create the appropriate GXT UI control for this text field.
     *
     * @return A new UI control.
     */
    protected TextField<String> createField() {
        return new TextField<String>();
    }


    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final Field<String> f = _field;
        final Paragraph p =
            Paragraph.fromText(getName(), f.getValue());
        return p;
    }


    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {
        final Field<String> f = _field;
        f.setValue(para.getText());
    }


    /** {@inheritDoc} */
    @Override
    public TextField<String> getUI() {
        return _field;
    }


    private String getValueText() {
        return _field.getValue();
    }


    /** {@inheritDoc} */
    @Override
    public void validate(final ValidationResult vResult) {
        final String fieldName = getName();
        final String fieldText = getValueText();
        if (null!= fieldText
            && fieldText.length() > Paragraph.MAX_TEXT_LENGTH) {
            vResult.addError(I18n.uiMessages.paragraphTooLarge(fieldName));
        }
    }
}
