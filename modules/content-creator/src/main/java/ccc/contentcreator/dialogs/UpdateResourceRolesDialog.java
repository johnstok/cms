/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.api.ID;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.IGlobalsImpl;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextArea;


/**
 * Dialog for updating the security roles associated with a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceRolesDialog
    extends
        AbstractEditDialog {

    private final ID _resourceId;
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
    public UpdateResourceRolesDialog(final ID resourceId,
                                     final Collection<String> currentRoles) {
        super(new IGlobalsImpl().uiConstants().updateRoles());
        _resourceId = resourceId;

        setWidth(DIALOG_WIDTH);
        setHeight(DIALOG_HEIGHT);

        _roles.setFieldLabel(_constants.roles());
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
                final Collection<String> validRoles = new ArrayList<String>();
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
                commands().changeRoles(
                    _resourceId,
                    validRoles,
                    new DisposingCallback(
                        UpdateResourceRolesDialog.this,
                        _constants.updateRoles())
                );
            }
        };
    }

}
