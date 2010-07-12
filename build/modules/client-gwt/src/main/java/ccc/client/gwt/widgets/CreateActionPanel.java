/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

package ccc.client.gwt.widgets;

import java.util.HashMap;
import java.util.Map;

import ccc.api.types.CommandType;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.extjs.gxt.ui.client.widget.DataListSelectionModel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;


/**
 * Panel used to create a new scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionPanel
    extends
        LayoutContainer {

    private final Globals _globals = new GlobalsImpl();
    private final UIConstants _uiConstants = I18n.UI_CONSTANTS;

    private final DataList _list = new DataList();
    private final LayoutContainer _parameters = new LayoutContainer();
    private ParameterPanel _pPanel;

    /**
     * Constructor.
     */
    public CreateActionPanel() {
        setLayout(new BorderLayout());

        _parameters.setId("action-parameters");
        _parameters.setLayout(new FormLayout());
        _parameters.setBorders(true);
        _parameters.setStyleAttribute("backgroundColor", "white");

        _list.setBorders(true);
        _list.setFlatStyle(true);
        _list.setSelectionMode(SelectionMode.SINGLE);
        final DataListSelectionModel sm = _list.getSelectionModel();
        _list.setSelectionModel(sm);
        _list.addListener(
            Events.SelectionChange,
            new Listener<ComponentEvent>() {
                public void handleEvent(final ComponentEvent ce) {
                    final DataList l = (DataList) ce.getComponent();
                    _parameters.removeAll();

                    final CommandType data =
                        l.getSelectedItem().<CommandType>getData("action-id");

                    switch (data) {
                        case RESOURCE_PUBLISH:
                            _pPanel = new EmptyPanel(
                                _uiConstants.publish(),
                                _uiConstants.publishesSelectedResource());
                            break;

                        case RESOURCE_UNPUBLISH:
                            _pPanel =
                                new EmptyPanel(
                                    _uiConstants.unpublish(),
                                    _uiConstants.unpublishesSelectedResource());
                            break;

                        case PAGE_UPDATE:
                            _pPanel = new UpdatePanel(_globals);
                            break;

                        default:
                            InternalServices.WINDOW.alert("Unsupported action!");
                            return;
                    }

                    _pPanel.populateForm(_parameters);
                    _parameters.layout();
                }
            }
        );
        final DataListItem publish = new DataListItem(_uiConstants.publish());
        publish.setData("action-id", CommandType.RESOURCE_PUBLISH);
        _list.add(publish);

        final DataListItem unpublish =
            new DataListItem(_uiConstants.unpublish());
        unpublish.setData("action-id", CommandType.RESOURCE_UNPUBLISH);
        _list.add(unpublish);

        final DataListItem update =
            new DataListItem(_uiConstants.updateContent());
        update.setData("action-id", CommandType.PAGE_UPDATE);
        _list.add(update);


        final BorderLayoutData westData =
            new BorderLayoutData(LayoutRegion.WEST, 205);
        westData.setMargins(new Margins(5, 0, 5, 5));
        add(_list, westData);

        final BorderLayoutData centerData =
            new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(5));
        add(_parameters, centerData);
    }

    /**
     * Accessor.
     *
     * @return The type of action to execute, as a string.
     */
    public CommandType commandType() {
        final DataListItem item = _list.getSelectedItem();
        if (null==item) {
            return null;
        }
        return item.<CommandType>getData("action-id");
    }

    /**
     * Accessor.
     *
     * @return A map of string parameters.
     */
    public Map<String, String> getParameters() {
        return (null==_pPanel)
            ? new HashMap<String, String>()
            : _pPanel.getParameters();
    }

    /**
     * API for parameter panels.
     */
    private static interface ParameterPanel {
        Map<String, String> getParameters();
        void populateForm(final LayoutContainer form);
    }

    /**
     * Parameter panel with no fields.
     */
    private static class EmptyPanel implements ParameterPanel {

        private final Html _title = new Html();

        EmptyPanel(final String title, final String description) {
            _title.setHtml(
                "<b>"+title+"</b><br><br>"
                + "<i>"+description+"</i><br><br>"
                + "<hr><br>No parameters required.");
        }

        /** {@inheritDoc} */
        @Override
        public Map<String, String> getParameters() {
            return new HashMap<String, String>();
        }

        /** {@inheritDoc} */
        @Override
        public void populateForm(final LayoutContainer form) {
            form.add(_title);
        }

    }

    /**
     * Parameter panel for revisions.
     */
    private static class UpdatePanel implements ParameterPanel {

        /** COMMENT_FIELD_HEIGHT : int. */
        private static final int COMMENT_FIELD_HEIGHT = 250;
        private final Html _title = new Html();
        private final CheckBox _majorEdit = new CheckBox();
        private final TextArea _comment = new TextArea();
        private final Globals _globals;

        /**
         * Constructor.
         *
         * @param globals The globals object.
         */
        public UpdatePanel(final Globals globals) {
            _globals = globals;
        }

        /** {@inheritDoc} */
        @Override
        public Map<String, String> getParameters() {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("MAJOR", _majorEdit.getValue().toString());
            params.put("COMMENT", _comment.getValue());
            return params;
        }

        /** {@inheritDoc} */
        public void populateForm(final LayoutContainer form) {
            final UIConstants uiConstants = I18n.UI_CONSTANTS;

            _title.setHtml("<b>"+uiConstants.update()+"</b><br><br><i>"
                +uiConstants.appliesTheSelectedResourcesWorkingCopy()
                +"</i><br><br><hr>");
            form.add(_title);

            _majorEdit.setFieldLabel(uiConstants.majorEdit());
            form.add(_majorEdit);

            _comment.setFieldLabel(uiConstants.comment());
            _comment.setHeight(COMMENT_FIELD_HEIGHT);
            form.add(_comment, new FormData("95%"));
        }

    }
}
