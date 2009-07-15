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


import static ccc.contentcreator.validation.Validations.*;
import ccc.contentcreator.actions.UpdateUserPasswordAction;
import ccc.contentcreator.binding.UserSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.validation.Validate;

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
        super(new IGlobalsImpl().uiConstants().editUserPw(),
              new IGlobalsImpl());

        _userDTO = userDTO;

        _username.setFieldLabel(constants().username());
        _username.setReadOnly(true);
        _username.setId(constants().username());
        _username.setValue(_userDTO.getUsername().toString());
        addField(_username);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setId(constants().password());
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setId(constants().confirmPassword());
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
                new UpdateUserPasswordAction(
                    _userDTO.getId(),
                    _password1.getValue()
                ) {
                    @Override protected void onNoContent(final Response response) {
                        close();
                    }
                }.execute();
            }
        };
    }
}
