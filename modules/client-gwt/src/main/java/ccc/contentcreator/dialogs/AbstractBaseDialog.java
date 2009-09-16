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
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.api.UIMessages;
import ccc.contentcreator.client.IGlobals;

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

    private final IGlobals _globals;
    private final UIConstants _constants;
    private final UIMessages  _messages;
    private final UIConstants _uiConstants;
    private final ActionNameConstants _userActions;

    private final Button _cancel;

    /**
     * Constructor.
     *
     * @param title The title of the dialog.
     * @param globals The globals for this dialog.
     */
    public AbstractBaseDialog(final String title, final IGlobals globals) {
        super();

        _globals = globals;
        _constants = _globals.uiConstants();
        _messages  = _globals.uiMessages();
        _uiConstants = _globals.uiConstants();
        _userActions = _globals.userActions();

        _cancel = cancelButton();

        setHeading(title);
        setWidth(IGlobals.DEFAULT_WIDTH);
        setMinWidth(IGlobals.MIN_WIDTH);
        setHeight(IGlobals.DEFAULT_HEIGHT);
        setLayout(new FitLayout());
        setBodyStyle("backgroundColor: white;");
        setMaximizable(true);
    }


    /**
     * Accessor.
     *
     * @return Returns the globals.
     */
    public IGlobals getGlobals() {
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
        cancel.setId("cancel");
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
        return _uiConstants;
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
