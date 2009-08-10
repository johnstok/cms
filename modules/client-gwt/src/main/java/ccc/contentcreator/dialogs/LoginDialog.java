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

import java.util.Map;

import ccc.contentcreator.actions.GetPropertyAction;
import ccc.contentcreator.actions.LoginAction;
import ccc.contentcreator.client.IGlobalsImpl;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.http.client.Response;


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

        final GetPropertyAction action = new GetPropertyAction() {
            /** {@inheritDoc} */
            @Override
            protected void onOK(final Response response) {
                final Map<String, String> map = parseMapString(response);

                LoginDialog.this.setHeading(
                    constants().login() +" - "+map.get("application.name"));
            }
        };
        action.execute();

        setPanelId("LoginPanel");

        _username.setFieldLabel(constants().username());
        _username.setId("username");
        _username.setAllowBlank(false);
        addField(_username);

        _password.setFieldLabel(constants().password());
        _password.setId("password");
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
    protected SelectionListener<ButtonEvent> saveAction() {
        return new ButtonSelectionListenerAction(new LoginAction(LoginDialog.this));
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
