/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import ccc.api.core.API;
import ccc.client.actions.LoginAction;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.gwt.widgets.ButtonSelectionListenerAction;
import ccc.client.views.LoginView;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog used to perform user login.
 *
 * @author Civic Computing Ltd.
 */
public class LoginDialog
    extends
        AbstractEditDialog
    implements
        LoginView {

    /** ENTER_KEY : int. */
    private static final int ENTER_KEY = 13;
    private static final int DIALOG_WIDTH = 375;
    private static final int DIALOG_HEIGHT = 380;

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _password = new TextField<String>();
    private final Text _message = new Text();


    /**
     * Constructor.
     */
    public LoginDialog() {
        super(I18n.uiConstants.login(), InternalServices.globals);
        LoginDialog.this.setHeading(
            constants().login()
            + " - "
            + getGlobals().getSetting(API.APPLICATION_NAME));


        final Html disclaimer = new Html(constants().disclaimer());
        getPanel().add(disclaimer);

        _username.setFieldLabel(constants().username());
        _username.setAllowBlank(false);
        _username.setSelectOnFocus(true);
        addField(_username);

        _password.setFieldLabel(constants().password());
        _password.setPassword(true);
        _password.setAllowBlank(false);
        addField(_password);

        getPanel().add(_message);

        getSave().setText(constants().login());
        getButtonBar().remove(getCancel());
        setClosable(false);
        setMaximizable(false);
        setHeight(DIALOG_HEIGHT);
        setWidth(DIALOG_WIDTH);

        _username.focus();
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new ButtonSelectionListenerAction(
            new LoginAction(LoginDialog.this));
    }


    /** {@inheritDoc} */
    @Override
    protected void onKeyPress(final WindowEvent we) {
        _message.setText("");
        if (we.getKeyCode() == ENTER_KEY) {
            we.preventDefault();
            new LoginAction(LoginDialog.this).execute();
        }
    }


    /** {@inheritDoc} */
    @Override
    public void loginFailed() {
        _message.setText(getUiConstants().loginFailed());
        _username.focus();
        _password.clear();
    }


    /** {@inheritDoc} */
    @Override
    public String getUsername() {
        return (null==_username.getValue()) ? "" : _username.getValue().trim();
    }


    /** {@inheritDoc} */
    @Override
    public String getPassword() {
        return (null==_password.getValue()) ? "" : _password.getValue().trim();
    }
}
