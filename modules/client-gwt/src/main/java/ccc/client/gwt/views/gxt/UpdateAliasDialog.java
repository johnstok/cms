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


import static ccc.client.core.InternalServices.*;

import java.util.UUID;

import ccc.api.core.Alias;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Response;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.remoting.UpdateAliasAction;

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

    private final ResourceSummary _alias;
    private UUID _targetId;
    private final ResourceSummary _targetRoot;

    /**
     * Constructor.
     *
     * @param alias The alias being edited.
     * @param targetName The name of the target resource.
     * @param targetRoot The root of the target resource.
     */
    public UpdateAliasDialog(final ResourceSummary alias,
                             final String targetName,
                             final ResourceSummary targetRoot) {
        super(I18n.UI_CONSTANTS.updateAlias(),
              new GlobalsImpl());

        _alias = alias;
        _targetRoot = targetRoot;
        setLayout(new FitLayout());

        setPanelId("AliasPanel");

        _aliasName.setFieldLabel(constants().name());
        _aliasName.setId("AliasName");
        _aliasName.setValue(_alias.getName());
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

                final ValidationResult vr = new ValidationResult();
                vr.addError(
                    VALIDATOR.notEmpty(
                        _aliasName.getValue(), _aliasName.getFieldLabel()));
                vr.addError(
                    VALIDATOR.notEmpty(
                        _targetName.getValue(), _targetName.getFieldLabel()));

                if (!vr.isValid()) {
                    InternalServices.WINDOW.alert(vr.getErrorText());
                    return;
                }

                createAlias();
            }
        };
    }


    private void createAlias() {
        // Target has not been changed.
        if (null == _targetId) {
            hide();
        } else {
            final Alias a = new Alias(_targetId);
            a.setId(_alias.getId());
            a.addLink(Resource.SELF, _alias.getLink(Resource.SELF));

            new UpdateAliasAction(a){
                /** {@inheritDoc} */
                @Override protected void onNoContent(final Response response) {
                    hide();
                }
            }.execute();
        }
    }
}
