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
package ccc.contentcreator.client.dialogs;


import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.client.Validate;
import ccc.contentcreator.client.Validations;
import ccc.contentcreator.dto.AliasDTO;
import ccc.contentcreator.dto.ResourceDTO;

import com.extjs.gxt.ui.client.Events;
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
public class EditAliasDialog extends AbstractEditDialog {

    private final TextField<String> _aliasName = new TextField<String>();
    private final TriggerField<String> _targetName =
        new TriggerField<String>();

    private ResourceDTO _target = null;
    private AliasDTO _alias = null;
    private final ResourceTable _rt;

    /**
     * Constructor.
     *
     * @param item The ResourceDTO
     * @param rt The resourceTable to refresh
     */
    public EditAliasDialog(final ResourceDTO item, final ResourceTable rt) {
        super(Globals.uiConstants().updateAlias());
        _rt = rt;
        _alias = (AliasDTO) item;
        setLayout(new FitLayout());

        setPanelId("AliasPanel");

        _aliasName.setFieldLabel(constants().name());
        _aliasName.setId("AliasName");
        _aliasName.setValue(item.getName());
        _aliasName.setReadOnly(true);
        _aliasName.disable();
        addField(_aliasName);

        _targetName.setFieldLabel(constants().target());
        _targetName.setValue("");
        _targetName.setId(constants().target());
        _targetName.setReadOnly(true);
        _targetName.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final ResourceSelectionDialog resourceSelect =
                        new ResourceSelectionDialog(Globals.resourceService());
                    resourceSelect.addListener(Events.Close,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent be) {
                            _target = resourceSelect.selectedResource();
                            _targetName.setValue(_target.getName());
                        }});
                    resourceSelect.show();
                }});
        addField(_targetName);

        resourceService().getResource(
            _alias.getTargetId(), new ErrorReportingCallback<ResourceDTO>() {

                public void onSuccess(final ResourceDTO result) {
                    _targetName.setValue(result.getName());
                }

            });
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
                Globals.resourceService().updateAlias(
                    _target,
                    _alias.getId(),
                    new ErrorReportingCallback<Void>(){
                        public void onSuccess(final Void result) {
                            _rt.refreshTable();
                            close();
                        }
                    });
            }
        };
    }
}
