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

import ccc.contentcreator.api.CommandService;
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.api.UIMessages;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.IGlobals;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;


/**
 * Base class for implementing dialogs.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractBaseDialog
    extends
        Window {

    protected final UIConstants _constants = Globals.uiConstants();
    protected final UIMessages  _messages  = Globals.uiMessages();

    private final QueriesServiceAsync _qs = GWT.create(QueriesService.class);
    private final CommandServiceAsync _cs = GWT.create(CommandService.class);

    protected final Button _cancel = cancelButton();

    /**
     * Constructor.
     *
     * @param title The title of the dialog.
     */
    public AbstractBaseDialog(final String title) {
        super();
        setHeading(title);
        setWidth(IGlobals.DEFAULT_WIDTH);
        setMinWidth(IGlobals.MIN_WIDTH);
        setHeight(IGlobals.DEFAULT_HEIGHT);
        setLayout(new FitLayout());
        setBodyStyle("backgroundColor: white;");
        setMaximizable(true);
    }


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
