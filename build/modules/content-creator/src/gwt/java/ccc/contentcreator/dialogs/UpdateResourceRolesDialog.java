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

import java.util.Arrays;
import java.util.Collection;

import ccc.contentcreator.callbacks.DisposingCallback;

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

    private final String _resourceId;
    private TextArea _roles = new TextArea();

    /**
     * Constructor.
     *
     * @param resourceId The resource whose roles will be updated.
     * @param currentRoles The roles the resource currently has.
     */
    public UpdateResourceRolesDialog(final String resourceId,
                                     final Collection<String> currentRoles) {
        super("Update roles"); // FIXME: I18n
        _resourceId = resourceId;

        _roles.setFieldLabel(_constants.roles());
        _roles.setId("resource-roles");
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
                final String[] roles =
                    _roles.getValue().split("\n"); // FIXME: what \r?
                // TODO: Trim whitespace / remove empty lines.
                commands().changeRoles(
                    _resourceId,
                    Arrays.asList(roles),
                    new DisposingCallback(UpdateResourceRolesDialog.this)
                );
            }
        };
    }

}
