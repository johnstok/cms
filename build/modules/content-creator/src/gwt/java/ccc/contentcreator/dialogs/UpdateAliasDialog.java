/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;


import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.services.api.AliasDelta;
import ccc.services.api.ID;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * Dialog for creating a new {@link Alias}.
 *
 * @author Civic Computing Ltd
 */
public class UpdateAliasDialog extends AbstractEditDialog {

    private final TextField<String> _aliasName = new TextField<String>();
    private final TriggerField<String> _targetName =
        new TriggerField<String>();

    private AliasDelta _alias = null;
    private ResourceSummary _targetRoot;
    private final ResourceTable _rt;

    /**
     * Constructor.
     *
     * @param delta The alias to edit
     * @param rt The resourceTable to refresh
     * @param targetRoot The root of the target resource
     */
    public UpdateAliasDialog(final AliasDelta delta,
                           final ResourceTable rt,
                           final ResourceSummary targetRoot) {
        super(Globals.uiConstants().updateAlias());
        _rt = rt;
        _alias = delta;
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
        _targetName.setValue(_alias.getTargetName());
        _targetName.setId(constants().target());
        _targetName.setReadOnly(true);
        _targetName.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final ResourceSelectionDialog resourceSelect =
                        new ResourceSelectionDialog(_targetRoot);
                    resourceSelect.addListener(Events.Close,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent be) {
                            final ModelData target =
                                resourceSelect.selectedResource();
                            _alias.setTargetId(target.<ID>get("id"));
                            _targetName.setValue(target.<String>get("name"));
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
                commands().updateAlias(
                    _alias,
                    new DisposingCallback(UpdateAliasDialog.this)
                );
            }
        };
    }
}
