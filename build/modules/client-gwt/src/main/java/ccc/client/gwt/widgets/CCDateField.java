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

import com.extjs.gxt.ui.client.widget.form.DateField;


/**
 * A date on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCDateField
    extends
        PageElement<DateField> {

    private DateField _dateField;

    /**
     * Constructor.
     *
     * @param name   The field's name.
     * @param title  The field's title.
     * @param desc   The field's description.
     */
    public CCDateField(final String name,
                       final String title,
                       final String desc) {
        super(name);

        final DateField df = new DateField();
        df.setFieldLabel(createLabel(name, title));
        df.setEditable(false);
        df.setToolTip(createTooltip(name, title, desc));

        _dateField = df;
    }

    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final DateField f = _dateField;
        if (null==f.getValue()) {
            return null;
        }
        final Paragraph p =
            Paragraph.fromDate(getName(), f.getValue());
        return p;
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {
        final DateField f = _dateField;
        f.setValue(para.getDate());
    }


    /** {@inheritDoc} */
    @Override
    public DateField getUI() {
        return _dateField;
    }
}
