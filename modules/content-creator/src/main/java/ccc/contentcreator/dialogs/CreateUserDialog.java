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
package ccc.contentcreator.dialogs;


import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SubmitControllerSelectionListener;
import ccc.contentcreator.controllers.CreateUserController;
import ccc.contentcreator.views.ICreateUserDialog;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for user creation.
 *
 * @author Civic Computing Ltd
 */
public class CreateUserDialog
    extends
        AbstractEditDialog
    implements
        ICreateUserDialog {

    private static final int LABEL_WIDTH = 150;
    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();

    /**
     * Constructor.
     */
    public CreateUserDialog() {
        super(Globals.uiConstants().createUser());

        setLabelWidth(LABEL_WIDTH); // Long labels, should fit to one line.

        _username.setFieldLabel(constants().username());
        _username.setAllowBlank(false);
        _username.setMinLength(IGlobals.MIN_USER_NAME_LENGTH);
        _username.setId(constants().username());
        addField(_username);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setId(constants().email());
        addField(_email);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setAllowBlank(false);
        _password1.setId(constants().password());
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setAllowBlank(false);
        _password2.setId(constants().confirmPassword());
        addField(_password2);

        setPanelId("UserPanel");
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SubmitControllerSelectionListener(
            new CreateUserController(this, new IGlobalsImpl()));
    }


    /** {@inheritDoc} */
    public final TextField<String> getUsername() { return _username; }


    /** {@inheritDoc} */
    public final TextField<String> getPassword1() { return _password1; }


    /** {@inheritDoc} */
    public final TextField<String> getPassword2() { return _password2; }


    /** {@inheritDoc} */
    public final TextField<String> getEmail() { return _email; }
}
