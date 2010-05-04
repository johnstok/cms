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
package ccc.client.gwt.views.gxt;


import static ccc.client.gwt.validation.Validations.*;
import ccc.api.core.User;
import ccc.client.gwt.binding.UserSummaryModelData;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.remoting.UpdateUserPasswordAction;
import ccc.client.gwt.validation.Validate;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.http.client.Response;


/**
 * Dialog for user password editing.
 *
 * @author Civic Computing Ltd
 */
public class EditUserPwDialog extends AbstractEditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();

    private final UserSummaryModelData _userDTO;

    /**
     * Constructor.
     *
     * @param userDTO The userDTO of the selected user.
     */
    public EditUserPwDialog(final UserSummaryModelData userDTO) {
        super(new GlobalsImpl().uiConstants().editUserPw(),
              new GlobalsImpl());

        _userDTO = userDTO;

        _username.setFieldLabel(constants().username());
        _username.setReadOnly(true);
        _username.setId("username");
        _username.setValue(_userDTO.getUsername().toString());
        addField(_username);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setId("user_password");
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setId("user_confirmPassword");
        addField(_password2);

        setPanelId("UserPwPanel");
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(updateUser())
                    .check(passwordStrength(_password1.getValue()))
                    .check(matchingPasswords(
                        _password1.getValue(), _password2.getValue()))
                    .callMethodOr(reportErrors());
            }
        };
    }


    /**
     * Updates the edited user and calls user list refreshing.
     *
     * @return
     */
    private Runnable updateUser() {
        return new Runnable() {
            public void run() {
                final User update = new User();
                update.setId(_userDTO.getId());
                update.setPassword(_password1.getValue());

                new UpdateUserPasswordAction(update) {
                    @Override protected void onNoContent(final Response r) {
                        hide();
                    }
                }.execute();
            }
        };
    }
}
