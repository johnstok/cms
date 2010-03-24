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
package ccc.contentcreator.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.contentcreator.core.Globals;
import ccc.contentcreator.core.GlobalsImpl;
import ccc.rest.dto.GroupDto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ListField;


/**
 * List Field for group listing.
 *
 * @author Civic Computing Ltd.
 */
public class GroupListField extends ListField<BaseModelData> {

    /** ROLE_HEIGHT : int. */
    private static final int ROLE_HEIGHT = 200;
    private final Globals _globals = new GlobalsImpl();

    /**
     * Constructor.
     *
     * @param allGroups The list of all groups.
     * @param selectedRoles UUIDs of selected groups.
     */
    public GroupListField(final Collection<GroupDto> allGroups,
                          final Set<UUID> selectedRoles) {
        final ListStore<BaseModelData> gData = new ListStore<BaseModelData>();
        final List<BaseModelData> selected = new ArrayList<BaseModelData>();
        for (final GroupDto g : allGroups) {
            final BaseModelData d = new BaseModelData();
            d.set("name", g.getName());
            d.set("id", g.getId());
            gData.add(d);
            if (selectedRoles.contains(g.getId())) { selected.add(d); }
        }

        setFieldLabel(_globals.uiConstants().roles());
        setHeight(ROLE_HEIGHT);
        setStore(gData);
        setSelection(selected);
        setDisplayField("name");
    }
}
