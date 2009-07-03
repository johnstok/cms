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
import ccc.contentcreator.client.IGlobals;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.user.client.ui.HTML;


/**
 * Dialog used to display error messages.
 *
 * @author Civic Computing Ltd.
 */
public class ErrorDialog extends AbstractEditDialog {
    private static final int DIALOG_WIDTH  = 375;
    private static final int DIALOG_HEIGHT = 300;

    private final TextArea _action = new TextArea();
    private final TextArea _error = new TextArea();
    private final TextArea _resolution = new TextArea();
    private final HTML _message = new HTML();

    private ErrorDialog(final String action,
                        final String error,
                        final String resolution,
                        final IGlobals globals) {
        super(globals.uiConstants().error(), globals);
        setPanelId("error-panel");

        _panel.add(_message);
        _message.setHTML(
            globals
                .errorDescriptions()
                .couldNotComplete("<b>", "</b><br><br>"));

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
    public ErrorDialog(final Throwable e,
                       final String action,
                       final IGlobals globals) {
        this(
            action,
            e.getMessage(),
            globals.errorResolutions().contactSysAdmin(),
            globals);
    }

    /**
     * Constructor.
     *
     * @param e The exception to display.
     * @param action The action that was performed.
     */
    public ErrorDialog(final CommandFailedException e,
                       final String action,
                       final IGlobals globals) {
        this(
            action,
            lookupError(e.getCode(), globals),
            lookupResolution(e.getCode(), globals),
            globals);
    }


    private static String lookupResolution(final int code,
                                           final IGlobals globals) {
        switch (code) {
            case Failure.UNLOCKED:
                return globals.errorResolutions().unlocked();
            case Failure.EXISTS:
                return globals.errorResolutions().exists();
            case Failure.LOCK_MISMATCH:
                return globals.errorResolutions().lockMismatch();
            case Failure.UNEXPECTED:
                return globals.errorResolutions().contactSysAdmin();
            case Failure.CYCLE:
                return globals.errorResolutions().cycle();
            default:
                return globals.errorResolutions().contactSysAdmin();
        }
    }


    private static String lookupError(final int code,
                                      final IGlobals globals) {
        switch (code) {
            case Failure.UNLOCKED:
                return globals.errorDescriptions().unlocked();
            case Failure.EXISTS:
                return globals.errorDescriptions().exists();
            case Failure.LOCK_MISMATCH:
                return globals.errorDescriptions().lockMismatch();
            case Failure.UNEXPECTED:
                return globals.errorDescriptions().unknown();
            case Failure.CYCLE:
                return globals.errorDescriptions().cycle();
            default:
                return globals.errorDescriptions().unknown();
        }
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return null;
    }
}
