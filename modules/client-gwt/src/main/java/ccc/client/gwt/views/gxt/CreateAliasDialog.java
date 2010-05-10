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


import ccc.api.core.ResourceSummary;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.Editable;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.ValidationResult;
import ccc.client.gwt.core.Validations2;
import ccc.client.gwt.views.CreateAlias;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;


/**
 * Dialog for creating a new Alias.
 *
 * @author Civic Computing Ltd
 */
public class CreateAliasDialog
    extends AbstractEditDialog
    implements CreateAlias {

    private final TextField<String> _targetName = new TextField<String>();
    private final TextField<String> _aliasName = new TextField<String>();
    private final TriggerField<String> _parentFolder =
        new TriggerField<String>();

    private ResourceSummaryModelData _parent = null;
    private Editable _presenter;
    private static final int DIALOG_HEIGHT = 200;

    /**
     * Constructor.
     *
     */
    public CreateAliasDialog() {

        super(new GlobalsImpl().uiConstants().createAlias(),
              new GlobalsImpl());
        setHeight(DIALOG_HEIGHT);

        _targetName.setFieldLabel(constants().target());
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
                getPresenter().save();
            }
        };
    }

    /**
     * Accessor.
     *
     * @return Returns the presenter.
     */
    Editable getPresenter() {
        return _presenter;
    }


    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        super.show();
    }


    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult result = new ValidationResult();

        if (!Validations2.notEmpty(_parentFolder.getValue())) {
            result.addError(
                _parentFolder.getFieldLabel()+getUiConstants().cannotBeEmpty());
        }
        if (!Validations2.notEmpty(_aliasName.getValue())) {
            result.addError(
                _aliasName.getFieldLabel()+getUiConstants().cannotBeEmpty());
        } else if (!Validations2.notValidResourceName(_aliasName.getValue())) {
            result.addError(getUiConstants().resourceNameIsInvalid());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String getAliasName() {
        return _aliasName.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public String getTargetName() {
        return _targetName.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public void setTargetName(final String targetName) {
        _targetName.setValue(targetName);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary getParent2() {
       return (null==_parent) ? null : _parent.getDelegate();
    }


    /** {@inheritDoc} */
    @Override
    public void alert(final String message) { getGlobals().alert(message); }
}
