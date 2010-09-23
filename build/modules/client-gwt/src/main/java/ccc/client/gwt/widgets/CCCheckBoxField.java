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

import java.util.ArrayList;
import java.util.List;

import ccc.api.types.Paragraph;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.Field;


/**
 * A check box on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCCheckBoxField
    extends
        PageElement<CheckBoxGroup> {

    private CheckBoxGroup _checkBoxGroup;


    /**
     * Constructor.
     *
     * @param name   The field's name.
     * @param title  The field's title.
     * @param desc   The field's description.
     * @param options The options that can be selected.
     */
    public CCCheckBoxField(final String name,
                           final String title,
                           final String desc,
                           final List<Option> options) {
        super(name);

        final CheckBoxGroup cbg =  new  CheckBoxGroup();
        cbg.setFieldLabel(createLabel(name, title));
        cbg.setToolTip(createTooltip(name, title, desc));
        cbg.setOrientation(Orientation.VERTICAL);
        cbg.setStyleAttribute("overflow", "hidden");

        for (final Option o : options) {
            final CheckBox cb = new CheckBox();

            cb.setBoxLabel(o.getTitle());
            cb.setId(o.getValue());
            cb.setValue(o.isDefault());

            cbg.add(cb);
        }

        _checkBoxGroup = cbg;
    }


    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final CheckBoxGroup cbg = _checkBoxGroup;
        final List<String> strings = new ArrayList<String>();

        for (final CheckBox cb : cbg.getValues()) {
            strings.add(cb.getId());
        }

        return Paragraph.fromList(getName(), strings);
    }


    /** {@inheritDoc} */
    @Override
    public CheckBoxGroup getUI() { return _checkBoxGroup; }


    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {
        final CheckBoxGroup cbg = _checkBoxGroup;
        final List<String> valueList = para.getList();

        for (final Field<?> f : cbg.getAll()) {
            if (f instanceof CheckBox) {
                final CheckBox box = (CheckBox) f;
                if (valueList.contains(box.getId())) {
                    box.setValue(Boolean.valueOf(true));
                } else {
                    box.setValue(Boolean.valueOf(false));
                }
            }
        }
    }
}
