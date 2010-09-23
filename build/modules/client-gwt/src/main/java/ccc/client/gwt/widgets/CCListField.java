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

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ListField;


/**
 * A list on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCListField
    extends
        PageElement<ListField<BaseModelData>> {

    private ListField<BaseModelData> _list;


    /**
     * Constructor.
     *
     * @param name    The field's name.
     * @param title   The field's title.
     * @param desc    The field's description.
     * @param options The options that can be selected.
     */
    public CCListField(final String name,
                       final String title,
                       final String desc,
                       final List<Option> options) {
        super(name);

        final ListField<BaseModelData> list = new ListField<BaseModelData>();
        list.setFieldLabel(createLabel(name, title));
        list.setToolTip(createTooltip(name, title, desc));
        list.setDisplayField("title");
        list.setValueField("value");

        final ListStore<BaseModelData> store =  new ListStore<BaseModelData>();
        final List<BaseModelData> selection = new ArrayList<BaseModelData>();

        for (final Option o : options) {
            final BaseModelData model = new BaseModelData();

            model.set("title", o.getTitle());
            model.set("value", o.getValue());
            store.add(model);
            if (o.isDefault().booleanValue()) {
                selection.add(model);
            }
        }
        list.setStore(store);
        list.addListener(Events.Render, new BugFixListener(selection, list));

        _list = list;
    }


    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final ListField<BaseModelData> list = _list;
        final List<String> strings = new ArrayList<String>();

        for (final BaseModelData item : list.getSelection()) {
            strings.add((String) item.get("value"));
        }

        return Paragraph.fromList(getName(), strings);
    }


    /** {@inheritDoc} */
    @Override
    public ListField<BaseModelData> getUI() { return _list; }


    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {
        final ListField<BaseModelData> list = _list;
        final List<String> valueList = para.getList();
        final List<BaseModelData> selection =
            new ArrayList<BaseModelData>();

        final ListStore<BaseModelData> items = list.getStore();
        for (final BaseModelData item : items.getModels()) {
            if (valueList.contains(item.get("value"))) {
                selection.add(item);
            }
        }
        list.removeAllListeners();
        list.addListener(Events.Render, new BugFixListener(selection, list));
    }


    /**
     * A listener to fix a bug in GXT's ListField.
     * See http://extjs.com/forum/showthread.php?t=55659 for more details.
     *
     * @author Civic Computing Ltd.
     */
    private static final class BugFixListener
        implements
            Listener<BaseEvent> {

        private final List<BaseModelData>      _selection;
        private final ListField<BaseModelData> _list;

        /**
         * Constructor.
         *
         * @param selection
         * @param list
         */
        BugFixListener(final List<BaseModelData> selection,
                       final ListField<BaseModelData> list) {

            _selection = selection;
            _list = list;
        }

        public void handleEvent(final BaseEvent baseEvent) {
            _list.setSelection(_selection);
        }
    }
}
