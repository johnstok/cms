/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.extjs.gxt.ui.client.widget.DataListSelectionModel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.Label;


/**
 * Panel used to create a new scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionPanel
    extends
        LayoutContainer {

    final DataList list = new DataList();

    /**
     * Constructor.
     */
    public CreateActionPanel() {
        setLayout(new BorderLayout());

        final LayoutContainer _parameters = new LayoutContainer();
        _parameters.setId("action-parameters");
        _parameters.setLayout(new FormLayout());
        _parameters.setBorders(true);
        _parameters.setStyleAttribute("backgroundColor", "white");

        list.setBorders(true);
        list.setFlatStyle(true);
        list.setSelectionMode(SelectionMode.SINGLE);
        final DataListSelectionModel sm = list.getSelectionModel();
        list.setSelectionModel(sm);
        list.addListener(
            Events.SelectionChange,
            new Listener<ComponentEvent>() {
                public void handleEvent(final ComponentEvent ce) {
                    final DataList l = (DataList) ce.component;
                    _parameters.removeAll();
                    _parameters.add(new Label(l.getSelectedItem().getText()));
                    _parameters.layout();
                }
            }
        );
        list.add(new DataListItem("Publish")); // FIXME: I18n
        list.add(new DataListItem("Unpublish")); // FIXME: I18n


        final BorderLayoutData westData =
            new BorderLayoutData(LayoutRegion.WEST, 205);
        westData.setMargins(new Margins(5, 0, 5, 5));
        add(list, westData);

        final BorderLayoutData centerData =
            new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(5));
        add(_parameters, centerData);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String actionType() {
        final DataListItem item = list.getSelectedItem();
        if (null==item) {
            return null;
        }
        return item.getText().toUpperCase();
    }
}
