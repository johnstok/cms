/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import ccc.contentcreator.actions.GetPropertyAction;
import ccc.contentcreator.actions.LoginAction;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SelectionListenerAction;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog used to perform user login.
 *
 * @author Civic Computing Ltd.
 */
public class LoginDialog extends AbstractEditDialog {
    private static final int DIALOG_WIDTH = 375;
    private static final int DIALOG_HEIGHT = 150;

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _password = new TextField<String>();
    private final Text _message = new Text();

    /**
     * Constructor.
     */
    public LoginDialog() {
        super(new IGlobalsImpl().uiConstants().login(), new IGlobalsImpl());

        new GetPropertyAction("application.name", this).execute();

        setPanelId("LoginPanel");

        _username.setFieldLabel(constants().username());
        _username.setId(constants().username());
        _username.setAllowBlank(false);
        addField(_username);

        _password.setFieldLabel(constants().password());
        _password.setId(constants().password());
        _password.setPassword(true);
        _password.setAllowBlank(false);
        addField(_password);

        _panel.add(_message);

        _save.setText(constants().login());
        getButtonBar().remove(_cancel);
        setClosable(false);
        setMaximizable(false);
        setHeight(DIALOG_HEIGHT);
        setWidth(DIALOG_WIDTH);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ComponentEvent> saveAction() {
        return new SelectionListenerAction(new LoginAction(LoginDialog.this));
    }

    public void loginFailed() {
        _message.setText(_constants.loginFailed());
    }

    public String getUsername() {
        return _username.getValue();
    }

    public String getPassword() {
        return _password.getValue();
    }
}
