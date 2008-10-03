/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;


import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dto.UserDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class CreateUserDialog extends EditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();


    /**
     * Constructor.
     */
    public CreateUserDialog() {

        setHeading(_constants.createUser());

        _username.setFieldLabel(_constants.username());
        _username.setAllowBlank(false);
        _username.setId(_constants.username());
        _panel.add(_username, new FormData("100%"));

        _email.setFieldLabel(_constants.email());
        _email.setAllowBlank(false);
        _email.setId(_constants.email());
        _panel.add(_email, new FormData("100%"));


        _panel.setId("UserPanel");
        _save.setId("userSave");
        _cancel.setId("userCancel");
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {

                final UserDTO userDto = new UserDTO();
                userDto.setUsername(_username.getValue());
                userDto.setEmail(_email.getValue());

                /*
                 * TODO Validation:
                 * email - not empty, valid
                 * username - not empty, unique
                 */

                Globals.resourceService().createUser(
                    userDto,
                    new ErrorReportingCallback<Void>() {
                        public void onSuccess(final Void result) {
                            // TODO: Refresh the main window.
                            hide();
                        }
                    }
                );
            }
        };
    }
}
