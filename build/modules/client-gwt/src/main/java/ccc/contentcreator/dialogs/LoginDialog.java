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
    /** DIALOG_WIDTH : int. */
    private static final int DIALOG_WIDTH = 375;
    /** DIALOG_HEIGHT : int. */
    private static final int DIALOG_HEIGHT = 150;

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _password = new TextField<String>();
    private final Text _message = new Text();

    /**
     * Constructor.
     */
    public LoginDialog() {
        super(new IGlobalsImpl().uiConstants().login(), new IGlobalsImpl());

        new GetPropertyAction("application.name") {
            /** {@inheritDoc} */
            @Override protected void onOK(final Response response) {
                setHeading(_constants.login() +" - "+response.getText());
            }
        }.execute();

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
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                new LoginAction(_username.getValue(), _password.getValue()) {
                    /** {@inheritDoc} */
                    @Override protected void onOK(final Response response) {
                        final boolean success = parseBoolean(response);
                        if (success) {
                            _globals.refresh();
                        } else {
                            _message.setText(constants().loginFailed());
                        }
                    }
                }.execute();
            }
        };
    }
}
