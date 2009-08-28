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


import ccc.api.ResourceSummary;
import ccc.contentcreator.actions.CreateAliasAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
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
        _parentFolder.setReadOnly(true);
        _parentFolder.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final FolderSelectionDialog folderSelect =
                        new FolderSelectionDialog(root);
                    folderSelect.addListener(Events.Hide,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent be2) {
                            _parent = folderSelect.selectedFolder();
                            _parentFolder.setValue(_parent.getName());
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
