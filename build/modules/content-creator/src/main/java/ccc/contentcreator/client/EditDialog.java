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

package ccc.contentcreator.client;

import ccc.contentcreator.api.UIConstants;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class EditDialog
    extends
        Window {

    /** DEFAULT_WIDTH : int. */
    protected static final int DEFAULT_WIDTH = 640;
    /** DEFAULT_HEIGHT : int. */
    protected static final int DEFAULT_HEIGHT = 480;

    /** _constants : UIConstants. */
    protected final UIConstants _constants = Globals.uiConstants();

    /** _panel : FormPanel. */
    protected final FormPanel _panel = new FormPanel();
    /** _save : Button. */
    protected final Button _save = new Button(
            _constants.save(),
            saveAction());
    /** _cancel : Button. */
    protected final Button _cancel = new Button(
            _constants.cancel(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    hide();
                }
            }
        );

    /**
     * Constructor.
     *
     */
    public EditDialog() {
        super();

        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);
        setLayout(new FitLayout());

        _panel.setWidth("100%");
        _panel.setBorders(false);
        _panel.setBodyBorder(false);
        _panel.setHeaderVisible(false);
        add(_panel);

        addButton(_cancel);
        addButton(_save);
    }

    /**
     * Factory for save actions.
     *
     * @return A selection listener for use by the save button.
     */
    protected abstract SelectionListener<ButtonEvent> saveAction();

}