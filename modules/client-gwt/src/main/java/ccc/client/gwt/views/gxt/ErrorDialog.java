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

import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.I18n;
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
    private static final int DIALOG_WIDTH  = 450;
    private static final int DIALOG_HEIGHT = 375;
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
        super(I18n.UI_CONSTANTS.error(), globals);

        getPanel().add(_message);
        _message.setHTML(
            I18n.ERROR_DESCRIPTIONS
                .couldNotComplete("<b>", "</b><br><br>"));

        _action.setFieldLabel(constants().action());
        _action.setReadOnly(true);
        _action.setValue(action);
        addField(_action);

        _error.setFieldLabel(constants().details());
        _error.setReadOnly(true);
        _error.setValue(error);
        addField(_error);

        _resolution.setFieldLabel(constants().resolution());
        _resolution.setReadOnly(true);
        _resolution.setValue(resolution);
        addField(_resolution);

        getButtonBar().remove(getSave());
        setHeight(DIALOG_HEIGHT);
        setWidth(DIALOG_WIDTH);

        addListener(Events.Resize, new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                final int newHeight = be.getHeight()-PANEL_HEIGHT;
                if (newHeight > DIALOG_MIN_HEIGHT) {
                    _action.setHeight(newHeight/3);
                    _error.setHeight(newHeight/3);
                    _resolution.setHeight(newHeight/3);
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
            I18n.ERROR_RESOLUTIONS.contactSysAdmin(),
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
            lookupError(e),
            lookupResolution(e),
            globals);
    }


    private static String lookupResolution(final RemoteException e) {

        final String code = e.getCode();

        if ("ccc.api.exceptions.UnlockedException".equals(code)) {
            return I18n.ERROR_RESOLUTIONS.unlocked();
        } else if ("ccc.api.exceptions.ResourceExistsException".equals(code)) {
            return I18n.ERROR_RESOLUTIONS.exists();
        } else if ("ccc.api.exceptions.LockMismatchException".equals(code)) {
            return I18n.ERROR_RESOLUTIONS.lockMismatch();
        } else if ("ccc.api.exceptions.CycleDetectedException".equals(code)) {
            return I18n.ERROR_RESOLUTIONS.cycle();
        } else if ("ccc.api.exceptions.InvalidException".equals(code)) {
            return I18n.ERROR_RESOLUTIONS.invalidCommand();
        }

        return I18n.ERROR_RESOLUTIONS.contactSysAdmin();
    }


    private static String lookupError(final RemoteException e) {

        final String code = e.getCode();

        if ("ccc.api.exceptions.UnlockedException".equals(code)) {
            return I18n.ERROR_DESCRIPTIONS.unlocked();
        } else if ("ccc.api.exceptions.ResourceExistsException".equals(code)) {
            return I18n.ERROR_DESCRIPTIONS.exists();
        } else if ("ccc.api.exceptions.LockMismatchException".equals(code)) {
            return I18n.ERROR_DESCRIPTIONS.lockMismatch();
        } else if ("ccc.api.exceptions.CycleDetectedException".equals(code)) {
            return I18n.ERROR_DESCRIPTIONS.cycle();
        } else if ("ccc.api.exceptions.InvalidException".equals(code)) {
            return I18n.ERROR_DESCRIPTIONS.invalidCommand();
        }

        return I18n.ERROR_DESCRIPTIONS.unknown()+"\n\n"+e.getMessage();
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return null;
    }
}
