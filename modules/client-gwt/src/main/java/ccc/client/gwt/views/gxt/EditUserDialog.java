/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Revision      $Rev: 467 $
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;



import static ccc.client.core.InternalServices.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.Group;
import ccc.api.core.User;
import ccc.api.types.CommandType;
import ccc.api.types.Username;
import ccc.client.actions.UpdateUserAction;
import ccc.client.core.DefaultCallback;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;
import ccc.client.events.Event;
import ccc.client.gwt.widgets.GroupListField;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for user editing.
 *
 * @author Civic Computing Ltd
 */
public class EditUserDialog
    extends
        AbstractEditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _name = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();

    private final ListField<BaseModelData> _groups;
    private final User _userDTO;

    /**
     * Constructor.
     *
     * @param userDTO The userDTO of the selected user.
     * @param allGroups The list of all groups.
     */
    public EditUserDialog(final User userDTO,
                          final Collection<Group> allGroups) {
        super(I18n.uiConstants.editUser(), InternalServices.globals);

        _userDTO   = userDTO;

        _username.setFieldLabel(constants().username());
        if (getGlobals().currentUser().getId().equals(userDTO.getId())) {
            _username.setReadOnly(true);
        }
        _username.setValue(_userDTO.getUsername().toString());
        addField(_username);

        _name.setFieldLabel(constants().fullName());
        _name.setAllowBlank(false);
        _name.setValue(_userDTO.getName());
        addField(_name);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setValue(_userDTO.getEmail());
        addField(_email);

        _groups = new GroupListField(allGroups, _userDTO.getGroups());
        addField(_groups);
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {

                final ValidationResult vr = new ValidationResult();
                vr.addError(
                    validator.notEmpty(
                        _email.getValue(), _email.getFieldLabel()));
                vr.addError(
                    validator.notEmpty(
                        _username.getValue(), _username.getFieldLabel()));
                vr.addError(
                    validator.notEmpty(
                        _name.getValue(), _name.getFieldLabel()));
                vr.addError(
                    validator.notValidEmail(
                        _email.getValue(), _email.getFieldLabel()));

                if (!vr.isValid()) {
                    InternalServices.window.alert(vr.getErrorText());
                    return;
                }

                updateUser();
            }
        };
    }


    /**
     * Updates the edited user and calls user list refreshing.
     *
     * @return
     */
    private void updateUser() {
        if (!getGlobals().currentUser().getId().equals(_userDTO.getId())) {
            _userDTO.setUsername(new Username(_username.getValue()));
        }
        _userDTO.setEmail(_email.getValue());
        _userDTO.setName(_name.getValue());

        final Set<UUID> validGroups = new HashSet<UUID>();
        for (final BaseModelData selected : _groups.getSelection()) {
            validGroups.add(selected.<UUID>get("id"));
        }
        _userDTO.setGroups(validGroups);

        new UpdateUserAction(_userDTO).execute(
            new DefaultCallback<User>(I18n.uiConstants.editUser()) {
            @Override public void onSuccess(final User result) {
                InternalServices.remotingBus.fireEvent(
                    new Event<CommandType>(CommandType.USER_UPDATE));
                hide();
            }
        });
    }
}
