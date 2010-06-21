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

import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.i18n.ActionNameConstants;
import ccc.client.i18n.UIConstants;
import ccc.client.i18n.UIMessages;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * Base class for implementing dialogs.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractBaseDialog
    extends
        Window {

    /** CONTEXT_MENU_WIDTH : int. */
    public static final int CONTEXT_MENU_WIDTH = 150;
    /** PERCENT_10 : float. */
    public static final float PERCENT_10 = .1f;
    /** PERCENT_30 : float. */
    public static final float PERCENT_30 = .3f;
    /** PERCENT_40 : float. */
    public static final float PERCENT_40 = .4f;
    /** PERCENT_50 : float. */
    public static final float PERCENT_50 = .5f;
    /** PERCENT_70 : float. */
    public static final float PERCENT_70 = .7f;

    private final Globals _globals;
    private final UIConstants _constants;
    private final UIMessages  _messages;
    private final ActionNameConstants _userActions;

    private final Button _cancel;

    /**
     * Constructor.
     *
     * @param title The title of the dialog.
     * @param globals The globals for this dialog.
     */
    public AbstractBaseDialog(final String title, final Globals globals) {
        super();

        _globals = globals;
        _constants = I18n.UI_CONSTANTS;
        _messages  = I18n.UI_MESSAGES;
        _userActions = GlobalsImpl.userActions();

        _cancel = cancelButton();

        setHeading(title);
        setWidth(Globals.DEFAULT_WIDTH);
        setMinWidth(Globals.MIN_WIDTH);
        setHeight(Globals.DEFAULT_HEIGHT);
        setLayout(new FitLayout());
        setBodyStyle("backgroundColor: white;");
        setMaximizable(true);
    }


    /**
     * Accessor.
     *
     * @return Returns the globals.
     */
    public Globals getGlobals() {
        return _globals;
    }


    /**
     * Accessor.
     *
     * @return Returns the _constants.
     */
    protected UIConstants constants() {
        return _constants;
    }


    /**
     * Accessor.
     *
     * @return Returns the _constants.
     */
    protected UIMessages messages() {
        return _messages;
    }


    /**
     * Creates a cancel button which closes the window on click.
     *
     * @return Cancel button.
     */
    private  Button cancelButton(){
        final Button cancel = new Button(
        constants().cancel(),
        new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(final ButtonEvent ce) {
                hide();
            }
        });
        return cancel;
    }


    /**
     * Accessor.
     *
     * @return Returns the cancel button.
     */
    protected Button getCancel() {
        return _cancel;
    }


    /**
     * Accessor.
     *
     * @return Returns the constants.
     */
    protected UIConstants getConstants() {
        return _constants;
    }


    /**
     * Accessor.
     *
     * @return Returns the messages.
     */
    protected UIMessages getMessages() {
        return _messages;
    }


    /**
     * Accessor.
     *
     * @return Returns the uiConstants.
     */
    protected UIConstants getUiConstants() {
        return _constants;
    }


    /**
     * Accessor.
     *
     * @return Returns the userActions.
     */
    protected ActionNameConstants getUserActions() {
        return _userActions;
    }
}
