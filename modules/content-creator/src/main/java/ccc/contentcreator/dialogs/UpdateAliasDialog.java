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


import ccc.api.AliasDelta;
import ccc.api.ID;
import ccc.api.ResourceSummary;
import ccc.contentcreator.actions.UpdateAliasAction_;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.http.client.Response;

/**
 * Dialog for creating a new {@link Alias}.
 *
 * @author Civic Computing Ltd
 */
public class UpdateAliasDialog extends AbstractEditDialog {

    private final TextField<String> _aliasName = new TextField<String>();
    private final TriggerField<String> _targetName =
        new TriggerField<String>();

    private final ID _aliasId;
    private final AliasDelta _alias;
    private final ResourceSummary _targetRoot;

    /**
     * Constructor.
     *
     * @param aliasId The id of the alias to edit.
     * @param aliasName The name of the alias being edited.
     * @param delta The changes to make.
     * @param targetRoot The root of the target resource
     */
    public UpdateAliasDialog(final ID aliasId,
                             final AliasDelta delta,
                             final String aliasName,
                             final ResourceSummary targetRoot) {
        super(new IGlobalsImpl().uiConstants().updateAlias(),
              new IGlobalsImpl());

        _aliasId = aliasId;
        _alias = delta;
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
        _targetName.setValue(_alias.getTargetName());
        _targetName.setId(constants().target());
        _targetName.setReadOnly(true);
        _targetName.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be1) {
                    final ResourceSelectionDialog resourceSelect =
                        new ResourceSelectionDialog(_targetRoot);
                    resourceSelect.addListener(Events.Close,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent be2) {
                            final ResourceSummaryModelData target =
                                resourceSelect.selectedResource();
                            if (target != null) {
                                _alias.setTargetId(target.getId());
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
                new UpdateAliasAction_(_aliasId, _alias){
                    /** {@inheritDoc} */
                    @Override protected void onNoContent(final Response response) {
                        close();
                    }
                }.execute();
            }
        };
    }
}
