/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;


import ccc.contentcreator.actions.CreateAliasAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.rest.dto.ResourceSummary;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.google.gwt.http.client.Response;


/**
 * Dialog for creating a new Alias.
 *
 * @author Civic Computing Ltd
 */
public class CreateAliasDialog extends AbstractEditDialog {

    private final TextField<String> _targetName = new TextField<String>();
    private final TextField<String> _aliasName = new TextField<String>();
    private final TriggerField<String> _parentFolder =
        new TriggerField<String>();

    private final SingleSelectionModel _ssm;
    private ResourceSummaryModelData _parent = null;

    /**
     * Constructor.
     *
     * @param ssm Selection model.
     * @param root Resource root for folder selection
     */
    public CreateAliasDialog(final SingleSelectionModel ssm,
                             final ResourceSummary root) {
        super(new IGlobalsImpl().uiConstants().createAlias(),
              new IGlobalsImpl());
        setPanelId("AliasPanel");

        _ssm = ssm;

        _targetName.setFieldLabel(constants().target());
        _targetName.setValue(_ssm.tableSelection().getName());
        _targetName.setReadOnly(true);
        _targetName.disable();
        addField(_targetName);

        _aliasName.setFieldLabel(constants().name());
        _aliasName.setAllowBlank(false);
        _aliasName.setId("AliasName");
        addField(_aliasName);

        _parentFolder.setFieldLabel(constants().folder());
        _parentFolder.setId("parent-folder");
        _parentFolder.setValue("");
        _parentFolder.setEditable(false);
        _parentFolder.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final FolderSelectionDialog folderSelect =
                        new FolderSelectionDialog();
                    folderSelect.addListener(Events.Hide,
                        new Listener<WindowEvent>() {
                        public void handleEvent(final WindowEvent be2) {
                            final Button b = be2.getButtonClicked();
                            if (null==b) { // 'X' button clicked.
                                return;
                            }
                            _parent = folderSelect.selectedFolder();
                            _parentFolder.setValue(
                                (null==_parent)
                                    ? null
                                    : _parent.getName());
                        }});
                    folderSelect.show();
                }});
        addField(_parentFolder);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(createAlias())
                    .check(Validations.notEmpty(_aliasName))
                    .check(Validations.notEmpty(_parentFolder))
                    .check(Validations.notValidResourceName(_aliasName))
                    .stopIfInError()
                    .check(Validations.uniqueResourceName(_parent, _aliasName))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable createAlias() {
        return new Runnable() {
            public void run() {
                new CreateAliasAction(
                    _parent.getId(),
                    _aliasName.getValue(),
                    _ssm.tableSelection().getId()
                ){
                    /** {@inheritDoc} */
                    @Override protected void onOK(final Response response) {
                        final ResourceSummary rs =
                            parseResourceSummary(response);
                        final ResourceSummaryModelData newAlias =
                            new ResourceSummaryModelData(rs);
                        _ssm.create(newAlias, _parent);
                        hide();
                    }
                }.execute();
            }
        };
    }
}
