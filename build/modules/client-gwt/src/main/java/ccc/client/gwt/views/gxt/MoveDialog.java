/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import static ccc.client.core.InternalServices.*;
import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.client.actions.MoveResourceAction;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.SingleSelectionModel;
import ccc.client.core.ValidationResult;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * Dialog used to move a resource.
 *
 * @author Civic Computing Ltd.
 */
public class MoveDialog extends AbstractEditDialog {


    private final TextField<String> _targetName = new TextField<String>();

    private final TriggerField<String> _parentFolder =
        new TriggerField<String>();

    private final ResourceSummary _target;
    private ResourceSummary _parent = null;

    private final SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param item The Resource item to move.
     * @param ssm The selection model.
     * @param root Resource root for the selection dialog.
     */
    public MoveDialog(final ResourceSummary item,
                      final SingleSelectionModel ssm,
                      final ResourceSummary root) {
        super(I18n.uiConstants.move(), InternalServices.globals);
        setHeight(Globals.DEFAULT_MIN_HEIGHT);
        _ssm = ssm;

        _target = item;
        setLayout(new FitLayout());

        setPanelId("MovePanel");

        _targetName.setFieldLabel(constants().target());
        _targetName.setValue(item.getName().toString());
        _targetName.setReadOnly(true);
        _targetName.disable();
        addField(_targetName);

        _parentFolder.setFieldLabel(constants().folder());
        _parentFolder.setId("parent-folder");
        _parentFolder.setValue("");
        _parentFolder.setEditable(false);
        _parentFolder.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final ResourceSelectionDialog folderSelect =
                        new ResourceSelectionDialog(root, ResourceType.FOLDER);

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
                if (null==_parent) { return; }

                final ValidationResult vr = new ValidationResult();
                vr.addError(
                    validator.notEmpty(
                        _parentFolder.getValue(),
                        _parentFolder.getFieldLabel()));

                if (!vr.isValid()) {
                    InternalServices.window.alert(vr.getErrorText());
                    return;
                }

                move();
            }
        };
    }

    private void move() {
        new MoveResourceAction(_target, _parent.getId()){
            /** {@inheritDoc} */
            @Override protected void onSuccess(final Void v) {
                _ssm.move(_target, _parent, _ssm.treeSelection());
                hide();
            }
        }.execute();
    }
}
