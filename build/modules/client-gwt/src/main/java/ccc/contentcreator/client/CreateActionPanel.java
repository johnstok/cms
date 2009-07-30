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

import java.util.HashMap;
import java.util.Map;

import ccc.api.CommandType;
import ccc.contentcreator.api.UIConstants;

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

    private final IGlobals _globals = new IGlobalsImpl();
    private final UIConstants _uiConstants = _globals.uiConstants();

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
                            _globals.alert("Unsupported action!");
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

    public Map<String, String> getParameters() {
        return (null==_pPanel)
            ? new HashMap<String, String>()
            : _pPanel.getParameters();
    }

    private static interface ParameterPanel {
        Map<String, String> getParameters();
        void populateForm(final LayoutContainer form);
    }

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

    private static class UpdatePanel implements ParameterPanel {

        private final Html _title = new Html();
        private final CheckBox _majorEdit = new CheckBox();
        private final TextArea _comment = new TextArea();
        private final IGlobals _globals;

        /**
         * Constructor.
         *
         * @param globals The globals object.
         */
        public UpdatePanel(final IGlobals globals) {
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
            final UIConstants uiConstants = _globals.uiConstants();

            _title.setHtml("<b>"+uiConstants.update()+"</b><br><br><i>"
                +uiConstants.appliesTheSelectedResourcesWorkingCopy()
                +"</i><br><br><hr>");
            form.add(_title);

            _majorEdit.setFieldLabel(uiConstants.majorEdit());
            form.add(_majorEdit);

            _comment.setFieldLabel(uiConstants.comment());
            _comment.setHeight(250);
            form.add(_comment, new FormData("95%"));
        }

    }
}
