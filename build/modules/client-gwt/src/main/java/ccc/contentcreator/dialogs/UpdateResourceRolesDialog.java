/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.contentcreator.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.contentcreator.actions.UpdateResourceRolesAction;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.rest.dto.AclDto;
import ccc.rest.dto.GroupDto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.google.gwt.http.client.Response;


/**
 * Dialog for updating the security roles associated with a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceRolesDialog
    extends
        AbstractEditDialog {

    private final UUID _resourceId;
    private final ListField<BaseModelData> _groups =
        new ListField<BaseModelData>();
    private final AclDto _acl;

    private static final int DIALOG_WIDTH = 400;
    private static final int DIALOG_HEIGHT = 300;
    private static final int ROLES_HEIGHT = 200;

    /**
     * Constructor.
     *
     * @param resourceId The resource whose roles will be updated.
     * @param acl The access control list for the resource.
     * @param allGroups A list of all groups available in the system.
     */
    public UpdateResourceRolesDialog(final UUID resourceId,
                                     final AclDto acl,
                                     final Collection<GroupDto> allGroups) {
        super(new IGlobalsImpl().uiConstants().updateRoles(),
              new IGlobalsImpl());
        _resourceId = resourceId;
        _acl = acl;

        setWidth(DIALOG_WIDTH);
        setHeight(DIALOG_HEIGHT);

        final ListStore<BaseModelData> gData = new ListStore<BaseModelData>();
        final List<BaseModelData> selected = new ArrayList<BaseModelData>();
        for (final GroupDto g : allGroups) {
            final BaseModelData d = new BaseModelData();
            d.set("name", g.getName());
            d.set("id", g.getId());
            gData.add(d);
            if (acl.getGroups().contains((g.getId()))) { selected.add(d); }
        }

        _groups.setFieldLabel(getUiConstants().roles());
        _groups.setHeight(ROLES_HEIGHT);
        _groups.setStore(gData);
        _groups.setSelection(selected);
        _groups.setDisplayField("name");
        addField(_groups);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(final ButtonEvent ce) {

                final List<UUID> newGroups = new ArrayList<UUID>();
                for (final BaseModelData selected : _groups.getSelection()) {
                    newGroups.add(selected.<UUID>get("id"));
                }

                final AclDto acl =
                    new AclDto()
                        .setGroups(newGroups)
                        .setUsers(_acl.getUsers());

                new UpdateResourceRolesAction(_resourceId, acl) {
                    /** {@inheritDoc} */
                    @Override
                    protected void onNoContent(final Response response) {
                        hide();
                    }
                }.execute();
            }
        };
    }
}
