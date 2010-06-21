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
package ccc.client.gwt.views.gxt;



import java.util.UUID;

import ccc.api.core.Alias;
import ccc.api.core.ResourceSummary;
import ccc.client.core.I18n;
import ccc.client.core.Response;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.remoting.UpdateAliasAction;
import ccc.client.gwt.validation.Validate;
import ccc.client.gwt.validation.Validations;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * Dialog for creating a new Alias.
 *
 * @author Civic Computing Ltd
 */
public class UpdateAliasDialog extends AbstractEditDialog {

    private final TextField<String> _aliasName = new TextField<String>();
    private final TriggerField<String> _targetName =
        new TriggerField<String>();

    private final UUID _aliasId;
    private UUID _targetId;
    private final ResourceSummary _targetRoot;

    /**
     * Constructor.
     *
     * @param aliasId The id of the alias to edit.
     * @param aliasName The name of the alias being edited.
     * @param targetName The target name.
     * @param targetRoot The root of the target resource
     */
    public UpdateAliasDialog(final UUID aliasId,
                             final String targetName,
                             final String aliasName,
                             final ResourceSummary targetRoot) {
        super(I18n.UI_CONSTANTS.updateAlias(),
              new GlobalsImpl());

        _aliasId = aliasId;
        _targetRoot = targetRoot;
        setLayout(new FitLayout());

        setPanelId("AliasPanel");

        _aliasName.setFieldLabel(constants().name());
        _aliasName.setId("AliasName");
        _aliasName.setValue(aliasName);
        _aliasName.setReadOnly(true);
        _aliasName.disable();
        addField(_aliasName);

        _targetName.setFieldLabel(constants().target());
        _targetName.setValue("");
        _targetName.setValue(targetName);
        _targetName.setId("target");
        _targetName.setEditable(false);
        _targetName.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be1) {
                    final ResourceSelectionDialog resourceSelect =
                        new ResourceSelectionDialog(_targetRoot);
                    resourceSelect.addListener(Events.Hide,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent be2) {
                            final ResourceSummaryModelData target =
                                resourceSelect.selectedResource();
                            if (target != null) {
                                _targetId = target.getId();
                                _targetName.setValue(target.getName());
                            }
                        }});
                    resourceSelect.show();
                }});
        addField(_targetName);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(createAlias())
                    .check(Validations.notEmpty(_aliasName))
                    .check(Validations.notEmpty(_targetName))
                    .stopIfInError()
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }


    private Runnable createAlias() {
        return new Runnable() {
            public void run() {
                // Target has not been changed.
                if (null == _targetId) {
                    hide();
                } else {
                    final Alias a = new Alias(_targetId);
                    a.setId(_aliasId);

                    new UpdateAliasAction(a){
                        /** {@inheritDoc} */
                        @Override protected void onNoContent(
                                                      final Response response) {
                            hide();
                        }
                    }.execute();
                }
            }
        };
    }
}
