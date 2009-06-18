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

import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.SecurityServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;


/**
 * Dialog used to perform user login.
 *
 * @author Civic Computing Ltd.
 */
public class LoginDialog extends AbstractEditDialog {
    /** DIALOG_WIDTH : int. */
    private static final int DIALOG_WIDTH = 300;

    /** DIALOG_HEIGHT : int. */
    private static final int DIALOG_HEIGHT = 150;

    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _password = new TextField<String>();
    private final Text _message = new Text();

    /**
     * Constructor.
     */
    public LoginDialog() {
        super(Globals.uiConstants().login());

        final SecurityServiceAsync ss = Globals.securityService();
        ss.readProperty("application.name",
            new ErrorReportingCallback<String>(USER_ACTIONS.readProperty()){
            @Override
            public void onSuccess(final String value) {
                setHeading(Globals.uiConstants().login() +" - "+value);
            }

        });

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


    /**
     * Add a field to this dialog.
     *
     * @param widget The widget to add.
     */
    @Override
    protected void addField(final Widget widget) {
        _panel.add(widget, new FormData("90%"));
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Globals.securityService().login(
                    _username.getValue(),
                    _password.getValue(),
                    new AsyncCallback<Boolean>(){
                        public void onFailure(final Throwable caught) {
                            Globals.unexpectedError(caught, _constants.login());
                        }
                        public void onSuccess(final Boolean loginSucceeded) {
                            if (loginSucceeded) {
                                Globals.refresh();
                            } else {
                                _message.setText(constants().loginFailed());
                            }
                        }
                });
            }
        };
    }
}
