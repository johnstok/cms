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

package ccc.contentcreator.dialogs;


import ccc.contentcreator.client.IGlobals;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.ui.Widget;


/**
 * Base class for implementing form dialogs.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractEditDialog
    extends
        AbstractBaseDialog {

    private final FormPanel _panel = new FormPanel();
    private final Button _save = new Button(
            constants().save(),
            saveAction());

    /**
     * Constructor.
     *
     * @param title Title for the dialog.
     * @param globals The globals for this dialog.
     */
    public AbstractEditDialog(final String title, final IGlobals globals) {
        super(title, globals);

        _save.setId("save");

        _panel.setWidth("100%");
        _panel.setBorders(false);
        _panel.setBodyBorder(false);
        _panel.setHeaderVisible(false);
        add(_panel);

        addButton(getCancel());
        addButton(_save);
    }


    /**
     * Add a field to this dialog.
     *
     * @param widget The widget to add.
     */
    protected void addField(final Widget widget) {
        _panel.add(widget, new FormData("95%"));
    }


    /**
     * Set the id for this dialog's form panel.
     *
     * @param id The id as a string.
     */
    protected void setPanelId(final String id) {
        _panel.setId(id);
    }


    /**
     * Set the width of labels on this dialog's form panel.
     *
     * @param width The width as an int.
     */
    protected void setLabelWidth(final int width) {
        _panel.setLabelWidth(width);
    }


    /**
     * Accessor.
     *
     * @return Returns the panel.
     */
    protected FormPanel getPanel() {
        return _panel;
    }


    /**
     * Accessor.
     *
     * @return Returns the save.
     */
    protected Button getSave() {
        return _save;
    }


    /**
     * Factory for save actions.
     *
     * @return A selection listener for use by the save button.
     */
    protected abstract SelectionListener<ButtonEvent> saveAction();
}
