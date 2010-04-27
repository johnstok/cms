/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import java.util.Collection;

import ccc.api.core.GroupDto;
import ccc.client.gwt.i18n.UIConstants;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;

/**
 * A simple group selector used by resource Access control.
 *
 * @author Civic Computing Ltd.
 */
class GroupACLSelector extends Window {
    private final UIConstants _constants;
    private final ListStore<ModelData> _store;
    private static final int WIDTH = 200;

    /**
     * Constructor.
     *
     * @param store The parent store.
     * @param allGroups All available groups.
     * @param constants UI constants.
     */
    public GroupACLSelector(final ListStore<ModelData> store,
                            final Collection<GroupDto> allGroups,
                            final UIConstants constants) {
        _constants = constants;
        _store = store;
        setWidth(WIDTH);
        setModal(true);
        setBodyStyle("backgroundColor: white;");
        setScrollMode(Scroll.AUTOY);
        setHeading(_constants.selectGroups());

        final ContentPanel panel = new ContentPanel();
        panel.setCollapsible(false);
        panel.setAnimCollapse(false);
        panel.setFrame(true);
        panel.setHeaderVisible(false);
        panel.setBodyBorder(false);

        final ListStore<ModelData> gData = new ListStore<ModelData>();
        for (final GroupDto g : allGroups) {
            final BaseModelData d = new BaseModelData();
            boolean contains = false;
            for (final ModelData m : _store.getModels()) {
                if (m.get("id") == g.getId()) {
                    contains = true;
                }
            }
            if (!contains) {
                d.set("name", g.getName());
                d.set("id", g.getId());
                gData.add(d);
            }
        }

        final CheckBoxListView<ModelData> view =
            new CheckBoxListView<ModelData>();
        view.setStore(gData);
        view.setDisplayProperty("name");
        panel.add(view);
        panel.addButton(new Button(_constants.add(),
            new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final ModelData m : view.getChecked()) {
                    _store.add(m);
                }
                hide();
            }

        }));
        add(panel);
    }
}
