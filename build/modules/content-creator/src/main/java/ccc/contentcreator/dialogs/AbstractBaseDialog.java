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
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesServiceAsync;
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
    public static final int CONTEXT_MENU_WIDTH = 130;
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

    protected final IGlobals _globals;
    protected final UIConstants _constants;
    protected final UIMessages  _messages;
    protected final UIConstants _uiConstants;
    protected final ActionNameConstants USER_ACTIONS;

    private final QueriesServiceAsync _qs;
    private final CommandServiceAsync _cs;

    protected final Button _cancel;

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
        USER_ACTIONS = _globals.userActions();

        _qs = _globals.queriesService();
        _cs = _globals.commandService();

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
     * Accessor.
     *
     * @return Returns a query service.
     */
    protected QueriesServiceAsync queries() {
        return _qs;
    }


    /**
     * Accessor.
     *
     * @return Returns a command service.
     */
    protected CommandServiceAsync commands() {
        return _cs;
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
                close();
            }
        });
        cancel.setId("cancel");
        return cancel;
    }
}
