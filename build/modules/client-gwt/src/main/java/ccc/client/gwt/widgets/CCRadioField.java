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

import java.util.List;

import ccc.api.types.Paragraph;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;


/**
 * A radio group on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCRadioField
    extends
        PageElement<RadioGroup> {

    private RadioGroup _radioGroup;


    /**
     * Constructor.
     *
     * @param name    The field's name.
     * @param title   The field's title.
     * @param desc    The field's description.
     * @param options The options that can be selected.
     */
    public CCRadioField(final String name,
                        final String title,
                        final String desc,
                        final List<Option> options) {
        super(name);

        final RadioGroup rg = new RadioGroup();
        rg.setFieldLabel(createLabel(name, title));
        rg.setToolTip(createTooltip(name, title, desc));
        rg.setOrientation(Orientation.VERTICAL);
        rg.setStyleAttribute("overflow", "hidden");

        for (final Option o : options) {
            final Radio r = new Radio();

            r.setBoxLabel(o.getTitle());
            r.setId(o.getValue());
            r.setValue(o.isDefault());

            rg.add(r);
        }

        _radioGroup = rg;
    }


    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final RadioGroup rg = _radioGroup;
        String selected = "";
        if (rg.getValue() != null) {
            selected = rg.getValue().getId();
        }

        final Paragraph p =
            Paragraph.fromText(getName(), selected);
        return p;
    }


    /** {@inheritDoc} */
    @Override
    public RadioGroup getUI() { return _radioGroup; }


    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {
        final RadioGroup rg = _radioGroup;
        final String value = para.getText();

        for (final Field<?> f : rg.getAll()) {
            if (f instanceof Radio) {
                final Radio radio = (Radio) f;
                if (radio.getId().equals(value)) {
                    radio.setValue(Boolean.valueOf(true));
                } else {
                    radio.setValue(Boolean.valueOf(false));
                }
            }
        }
    }
}
