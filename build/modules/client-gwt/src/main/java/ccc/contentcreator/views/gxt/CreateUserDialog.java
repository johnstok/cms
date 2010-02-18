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
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.views.gxt;


import ccc.contentcreator.client.SubmitControllerSelectionListener;
import ccc.contentcreator.core.IGlobals;
import ccc.contentcreator.core.IGlobalsImpl;
import ccc.contentcreator.presenters.CreateUserPresenter;
import ccc.contentcreator.views.CreateUser;

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
        CreateUser {

    private static final int LABEL_WIDTH = 150;
    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _name = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();

    /**
     * Constructor.
     */
    public CreateUserDialog() {
        super(new IGlobalsImpl().uiConstants().createUser(),
             new IGlobalsImpl());

        setLabelWidth(LABEL_WIDTH); // Long labels, should fit to one line.

        _username.setFieldLabel(constants().username());
        _username.setAllowBlank(false);
        _username.setMinLength(IGlobals.MIN_USER_NAME_LENGTH);
        _username.setId("username");
        addField(_username);

        _name.setFieldLabel(constants().fullName());
        _name.setAllowBlank(false);
        _name.setMinLength(IGlobals.MIN_USER_NAME_LENGTH);
        _name.setId("name");
        addField(_name);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setId("user_email");
        addField(_email);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setAllowBlank(false);
        _password1.setId("user_password");
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setAllowBlank(false);
        _password2.setId("user_confirmPassword");
        addField(_password2);

        setPanelId("UserPanel");
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SubmitControllerSelectionListener(
            new CreateUserPresenter(this, new IGlobalsImpl()));
    }


    /** {@inheritDoc} */
    public final TextField<String> getUsername() { return _username; }


    /** {@inheritDoc} */
    public final TextField<String> getName() { return _name; }


    /** {@inheritDoc} */
    public final TextField<String> getPassword1() { return _password1; }


    /** {@inheritDoc} */
    public final TextField<String> getPassword2() { return _password2; }


    /** {@inheritDoc} */
    public final TextField<String> getEmail() { return _email; }
}
