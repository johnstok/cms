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
import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.client.core.Editable;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;
import ccc.client.views.CreateAlias;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
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

    private ResourceSummary _parent = null;
    private Editable _presenter;
    private static final int DIALOG_HEIGHT = 200;

    /**
     * Constructor.
     *
     */
    public CreateAliasDialog() {

        super(I18n.uiConstants.createAlias(), InternalServices.globals);
        setHeight(DIALOG_HEIGHT);

        _targetName.setFieldLabel(constants().target());
        _targetName.setReadOnly(true);
        _targetName.disable();
        addField(_targetName);

        _aliasName.setFieldLabel(constants().name());
        _aliasName.setAllowBlank(false);
        addField(_aliasName);

        _parentFolder.setFieldLabel(constants().folder());
        _parentFolder.setValue("");
        _parentFolder.setEditable(false);
        _parentFolder.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    ResourceSummary root = null;
                    for (final ResourceSummary rr : InternalServices.roots.getElements()) {
                        if (rr.getName().toString().equals("content")) {
                            root = rr;
                        }
                    }

                   final ResourceSelectionDialog folderSelect =
                       new ResourceSelectionDialog(root,
                           ResourceType.FOLDER);
                   folderSelect.addListener(Events.Hide,
                       new Listener<ComponentEvent>() {
                       public void handleEvent(final ComponentEvent ce) {
                           final ResourceSummary _md = folderSelect.selectedResource();
                           if (_md != null
                                   && _md.getType() != ResourceType.RANGE_FOLDER) {

                               _parent = _md;
                               _parentFolder.setValue(
                                   (null==_parent)
                                   ? null
                                       : _parent.getName().toString());
                           }
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

        result.addError(
            validator.notEmpty(
                _parentFolder.getValue(), _parentFolder.getFieldLabel()));
        result.addError(
            validator.notEmpty(
                _aliasName.getValue(), _aliasName.getFieldLabel()));
        result.addError(
            validator.notValidResourceName(
                _aliasName.getValue(), _aliasName.getFieldLabel()));

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
       return (null==_parent) ? null : _parent;
    }


    /** {@inheritDoc} */
    @Override
    public void alert(final String message) {
        InternalServices.window.alert(message);
    }
}
