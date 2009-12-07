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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextArea;
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
    private final TextArea _roles = new TextArea();

    private static final int DIALOG_WIDTH = 400;
    private static final int DIALOG_HEIGHT = 300;
    private static final int ROLES_HEIGHT = 200;

    /**
     * Constructor.
     *
     * @param resourceId The resource whose roles will be updated.
     * @param currentRoles The roles the resource currently has.
     */
    public UpdateResourceRolesDialog(final UUID resourceId,
                                     final Collection<String> currentRoles) {
        super(new IGlobalsImpl().uiConstants().updateRoles(),
              new IGlobalsImpl());
        _resourceId = resourceId;

        setWidth(DIALOG_WIDTH);
        setHeight(DIALOG_HEIGHT);

        _roles.setFieldLabel(getUiConstants().roles());
        _roles.setId("resource-roles");
        _roles.setHeight(ROLES_HEIGHT);
        final StringBuilder rolesString = new StringBuilder();
        for (final String role : currentRoles) {
            rolesString.append(role);
            rolesString.append('\n');
        }
        _roles.setValue(rolesString.toString());
        addField(_roles);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(final ButtonEvent ce) {
                final List<String> validRoles = new ArrayList<String>();
                String roleString = _roles.getValue();
                if (null==roleString) {
                    roleString = "";
                }
                final String[] roles =
                    roleString.split("\n|\r|\r\n");
                for (final String role : roles) {
                    final String cleanRole = role.trim();
                    if (cleanRole.length() > 0) {
                        validRoles.add(cleanRole);
                    }
                }

                new UpdateResourceRolesAction(_resourceId, validRoles) {
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
