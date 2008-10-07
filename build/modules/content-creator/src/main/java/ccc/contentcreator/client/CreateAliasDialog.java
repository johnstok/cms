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
package ccc.contentcreator.client;


import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dto.AliasDTO;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.ResourceDTO;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;


/**
 * Dialog for creating a new {@link Alias}.
 *
 * @author Civic Computing Ltd
 */
public class CreateAliasDialog extends Window {

    private final TextField<String> _targetName = new TextField<String>();
    private final TextField<String> _aliasName = new TextField<String>();
    private final TriggerField<String> _parentFolder =
        new TriggerField<String>();

    private final ResourceDTO _target;
    private FolderDTO _parent = null;
    private final UIConstants _constants;

    /**
     * Constructor.
     *
     * @param item The ResourceDTO
     */
    public CreateAliasDialog(final ResourceDTO item) {
        _target = item;
        _constants = Globals.uiConstants();
        setHeading(_constants.updateContent());
        setWidth(640);
        setHeight(480);
        setLayout(new FitLayout());

        final FormPanel panel = new FormPanel();
        panel.setWidth("100%");
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setHeaderVisible(false);
        panel.setId("AliasPanel");
        add(panel);

        _targetName.setFieldLabel(_constants.target());
        _targetName.setValue(item.getName());
        _targetName.setReadOnly(true);
        panel.add(_targetName, new FormData("100%"));

        _aliasName.setFieldLabel(_constants.name());
        _aliasName.setAllowBlank(false);
        _aliasName.setId("AliasName");
        panel.add(_aliasName, new FormData("100%"));

        _parentFolder.setFieldLabel(_constants.folder());
        _parentFolder.setValue("");
        _parentFolder.setReadOnly(true);
        _parentFolder.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final FolderSelectionDialog folderSelect =
                        new FolderSelectionDialog(Globals.resourceService());
                    folderSelect.addListener(Events.Close,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent be) {
                            _parent = folderSelect.selectedFolder();
                            _parentFolder.setValue(_parent.getName());
                        }});
                    folderSelect.show();
                }});
        panel.add(_parentFolder, new FormData("100%"));

        final Button cancel = new Button(
            _constants.cancel(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    hide();
                }
            }
        );
        cancel.setId("aliasCancel");
        addButton(cancel);

        final Button save = new Button(
            _constants.save(),
            new SelectionListener<ButtonEvent>() {
                @Override public void componentSelected(final ButtonEvent ce) {
                    Validate.callTo(createAlias())
                        .check(Validations.notEmpty(_aliasName))
                        .check(Validations.notEmpty(_parentFolder))
                        .stopIfInError()
                        .check(uniqueResourceName(_parentFolder, _aliasName))
                        .callMethodOr(Validations.reportErrors());
                }
            }
        );
        save.setId("aliasSave");
        addButton(save);
    }

    private Validator uniqueResourceName(final TriggerField<String> folder,
                                         final TextField<String> name) {

        return new Validator() {
            public void validate(final Validate validate) {
                Globals.resourceService().nameExistsInFolder(
                    _parent,
                    _aliasName.getValue(),
                    new ErrorReportingCallback<Boolean>(){
                        public void onSuccess(final Boolean nameExists) {
                            if (nameExists) {
                                validate.addMessage(
                                    "A resource with name '"
                                    + name.getValue()
                                    + "' already exists in this folder."
                                );
                            }
                            validate.next();
                        }});
            }

        };
    }

    private Runnable createAlias() {
        return new Runnable() {
            public void run() {
                Globals.resourceService().createAlias(
                    _parent,
                    new AliasDTO(
                        _aliasName.getValue(),
                        _aliasName.getValue(),
                        _target.getId()),
                    new DisposingCallback(CreateAliasDialog.this));
            }
        };
    }
}
