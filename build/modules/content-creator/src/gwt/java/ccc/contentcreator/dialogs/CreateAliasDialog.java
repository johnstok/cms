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
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;


/**
 * Dialog for creating a new {@link Alias}.
 *
 * @author Civic Computing Ltd
 */
public class CreateAliasDialog extends AbstractEditDialog {

    private final TextField<String> _targetName = new TextField<String>();
    private final TextField<String> _aliasName = new TextField<String>();
    private final TriggerField<String> _parentFolder =
        new TriggerField<String>();

    private final ModelData _target;
    private ModelData _parent = null;

    /**
     * Constructor.
     *
     * @param root Resource root for folder selection
     * @param item The ResourceDTO
     */
    public CreateAliasDialog(final ModelData item, final ResourceSummary root) {
        super(Globals.uiConstants().createAlias());
        setPanelId("AliasPanel");

        _target = item;

        _targetName.setFieldLabel(constants().target());
        _targetName.setValue(item.<String>get("name"));
        _targetName.setReadOnly(true);
        _targetName.disable();
        addField(_targetName);

        _aliasName.setFieldLabel(constants().name());
        _aliasName.setAllowBlank(false);
        _aliasName.setId("AliasName");
        addField(_aliasName);

        _parentFolder.setFieldLabel(constants().folder());
        _parentFolder.setValue("");
        _parentFolder.setReadOnly(true);
        _parentFolder.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final FolderSelectionDialog folderSelect =
                        new FolderSelectionDialog(root);
                    folderSelect.addListener(Events.Close,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent be) {
                            _parent = folderSelect.selectedFolder();
                            _parentFolder.setValue(_parent.<String>get("name"));
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
                Globals.commandService().createAlias(
                    _parent.<String>get("id"),
                    _aliasName.getValue(),
                    _target.<String>get("id"),
                    new DisposingCallback(CreateAliasDialog.this));
            }
        };
    }
}
