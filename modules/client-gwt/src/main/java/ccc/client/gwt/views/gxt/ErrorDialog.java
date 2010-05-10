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

import ccc.api.types.FailureCode;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.RemoteException;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
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
    private static final int DIALOG_MIN_HEIGHT = 100;
    private static final int PANEL_HEIGHT = 140;

    private final TextArea _action = new TextArea();
    private final TextArea _error = new TextArea();
    private final TextArea _resolution = new TextArea();
    private final HTML _message = new HTML();

    private ErrorDialog(final String action,
                        final String error,
                        final String resolution,
                        final Globals globals) {
        super(GlobalsImpl.uiConstants().error(), globals);
        setPanelId("error-panel");

        getPanel().add(_message);
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

        getButtonBar().remove(getSave());
        setHeight(DIALOG_HEIGHT);
        setWidth(DIALOG_WIDTH);

        addListener(Events.Resize, new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                final int height = be.getHeight()-PANEL_HEIGHT;
                if (height > DIALOG_MIN_HEIGHT) {
                    _action.setHeight(height/3);
                    _error.setHeight(height/3);
                    _resolution.setHeight(height/3);
                }
            }
        });
    }

    /**
     * Constructor.
     *
     * @param e The exception to display.
     * @param action The action that was performed.
     * @param globals IGlobals implementation.
     */
    public ErrorDialog(final Throwable e,
                       final String action,
                       final Globals globals) {
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
     * @param globals IGlobals implementation.
     */
    public ErrorDialog(final RemoteException e,
                       final String action,
                       final Globals globals) {
        this(
            action,
            lookupError(e.getCode(), globals),
            lookupResolution(e.getCode(), globals),
            globals);
    }


    private static String lookupResolution(final FailureCode code,
                                           final Globals globals) {
        switch (code) {
            case UNLOCKED:
                return globals.errorResolutions().unlocked();
            case EXISTS:
                return globals.errorResolutions().exists();
            case LOCK_MISMATCH:
                return globals.errorResolutions().lockMismatch();
            case UNEXPECTED:
                return globals.errorResolutions().contactSysAdmin();
            case CYCLE:
                return globals.errorResolutions().cycle();
            case INVALID:
                return globals.errorResolutions().invalidCommand();
            default:
                return globals.errorResolutions().contactSysAdmin();
        }
    }


    private static String lookupError(final FailureCode code,
                                      final Globals globals) {
        switch (code) {
            case UNLOCKED:
                return globals.errorDescriptions().unlocked();
            case EXISTS:
                return globals.errorDescriptions().exists();
            case LOCK_MISMATCH:
                return globals.errorDescriptions().lockMismatch();
            case UNEXPECTED:
                return globals.errorDescriptions().unknown();
            case CYCLE:
                return globals.errorDescriptions().cycle();
            case INVALID:
                return globals.errorDescriptions().invalidCommand();
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
