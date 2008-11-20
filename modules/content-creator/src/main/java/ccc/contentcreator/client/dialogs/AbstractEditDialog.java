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

package ccc.contentcreator.client.dialogs;


import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
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

    /** _panel : FormPanel. */
    private final FormPanel _panel = new FormPanel();

    /** _save : Button. */
    private final Button _save = new Button(
            constants().save(),
            saveAction());

    /** _cancel : Button. */
    private final Button _cancel = new Button(
        constants().cancel(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    close();
                }
            }
        );


    /**
     * Constructor.
     *
     * @param title Title for the dialog.
     */
    public AbstractEditDialog(final String title) {
        super(title);

        _save.setId("save");
        _cancel.setId("cancel");

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
     * Factory for save actions.
     *
     * @return A selection listener for use by the save button.
     */
    protected abstract SelectionListener<ButtonEvent> saveAction();
}
