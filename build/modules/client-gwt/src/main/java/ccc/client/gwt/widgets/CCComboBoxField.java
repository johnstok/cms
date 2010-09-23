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
import ccc.client.widgets.Option;
import ccc.client.widgets.PageElement;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;


/**
 * A combo box on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCComboBoxField
    extends
        PageElement<ComboBox<BaseModelData>> {

    private ComboBox<BaseModelData> _combobox;


    /**
     * Constructor.
     *
     * @param name    The field's name.
     * @param title   The field's title.
     * @param desc    The field's description.
     * @param options The options that can be selected.
     */
    public CCComboBoxField(final String name,
                           final String title,
                           final String desc,
                           final List<Option> options) {
        super(name);

        final ComboBox<BaseModelData> cb = new ComboBox<BaseModelData>();
        cb.setTriggerAction(TriggerAction.ALL);
        cb.setFieldLabel(createLabel(name, title));
        cb.setToolTip(createTooltip(name, title, desc));
        cb.setDisplayField("title");
        cb.setValueField("value");
        cb.setEditable(false);

        final ListStore<BaseModelData> store =  new ListStore<BaseModelData>();
        for (final Option o : options) {
            final BaseModelData model = new BaseModelData();

            model.set("title", o.getTitle());
            model.set("value", o.getValue());
            store.add(model);
            if (o.isDefault().booleanValue()) {
                cb.setValue(model);
            }
        }
        cb.setStore(store);

        _combobox = cb;
    }


    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final ComboBox<BaseModelData> cb = _combobox;
        String selected = "";
        if (cb.getValue() != null) {
            selected = cb.getValue().get("value");
        }

        final Paragraph p =
            Paragraph.fromText(getName(), selected);
        return p;
    }


    /** {@inheritDoc} */
    @Override
    public ComboBox<BaseModelData> getUI() { return _combobox; }


    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {

        final ComboBox<BaseModelData> cb = _combobox;
        final String value = para.getText();

        final ListStore<BaseModelData> store = cb.getStore();
        for (final BaseModelData model : store.getModels()) {
            if (model.get("value").equals(value)) {
                cb.setValue(model);
            }
        }
    }
}
