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

import ccc.api.CommandFailedException;
import ccc.api.Failure;
import ccc.contentcreator.api.ErrorDescriptions;
import ccc.contentcreator.api.ErrorResolutions;
import ccc.contentcreator.client.Globals;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;


/**
 * Dialog used to display error messages.
 *
 * @author Civic Computing Ltd.
 */
public class ErrorDialog extends AbstractEditDialog {
    private static final int DIALOG_WIDTH  = 350;
    private static final int DIALOG_HEIGHT = 300;
    private static final ErrorDescriptions ERR_DESCRIPTIONS =
        GWT.create(ErrorDescriptions.class);
    private static final ErrorResolutions ERR_RESOLUTIONS =
        GWT.create(ErrorResolutions.class);


    private final TextArea _action = new TextArea();
    private final TextArea _error = new TextArea();
    private final TextArea _resolution = new TextArea();
    private final HTML _message = new HTML();

    private ErrorDialog(final String action,
                        final String error,
                        final String resolution) {
        super(Globals.uiConstants().error());
        setPanelId("error-panel");

        _panel.add(_message);
        _message.setHTML(
            ERR_DESCRIPTIONS.couldNotComplete("<b>", "</b><br><br>"));

        _action.setFieldLabel(constants().action());
        _action.setId("error-action");
        _action.setReadOnly(true);
        _action.setValue(action);
        addField(_action);

        _error.setFieldLabel(constants().details());
        _error.setId("error-details");
        _error.setReadOnly(true);
        _error.setValue(error);
        addField(_error);

        _resolution.setFieldLabel(constants().resolution());
        _resolution.setId("error-resolution");
        _resolution.setReadOnly(true);
        _resolution.setValue(resolution);
        addField(_resolution);

        getButtonBar().remove(_save);
        setHeight(DIALOG_HEIGHT);
        setWidth(DIALOG_WIDTH);
    }

    /**
     * Constructor.
     *
     * @param e The exception to display.
     * @param action The action that was performed.
     */
    public ErrorDialog(final Throwable e, final String action) {
        this(
            action,
            e.getMessage(),
            ERR_RESOLUTIONS.contactSysAdmin());
    }

    /**
     * Constructor.
     *
     * @param e The exception to display.
     * @param action The action that was performed.
     */
    public ErrorDialog(final CommandFailedException e, final String action) {
        this(
            action,
            lookupError(e.getCode()),
            lookupResolution(e.getCode()));
    }


    private static String lookupResolution(final int code) {
        switch (code) {
            case Failure.UNLOCKED:
                return ERR_RESOLUTIONS.unlocked();
            default:
                return ERR_RESOLUTIONS.contactSysAdmin();
        }
    }


    private static String lookupError(final int code) {
        switch (code) {
            case Failure.UNLOCKED:
                return ERR_DESCRIPTIONS.unlocked();
            default:
                return ERR_DESCRIPTIONS.unknown();
        }
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return null;
    }
}
