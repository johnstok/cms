/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import ccc.contentcreator.actions.MoveResourceAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.rest.dto.ResourceSummary;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.http.client.Response;


/**
 * Dialog used to move a resource.
 *
 * @author Civic Computing Ltd.
 */
public class MoveDialog extends AbstractEditDialog {


    private final TextField<String> _targetName = new TextField<String>();

    private final TriggerField<String> _parentFolder =
        new TriggerField<String>();

    private final ResourceSummaryModelData _target;
    private ResourceSummaryModelData _parent = null;

    private final SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param item The Resource item to move.
     * @param ssm The selection model.
     * @param root Resource root for the selection dialog.
     */
    public MoveDialog(final ResourceSummaryModelData item,
                      final SingleSelectionModel ssm,
                      final ResourceSummary root) {
        super(new IGlobalsImpl().uiConstants().move(), new IGlobalsImpl());
        setHeight(IGlobals.DEFAULT_MIN_HEIGHT);
        _ssm = ssm;

        _target = item;
        setLayout(new FitLayout());

        setPanelId("MovePanel");

        _targetName.setFieldLabel(constants().target());
        _targetName.setValue(item.getName());
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
                    final FolderSelectionDialog folderSelect =
                        new FolderSelectionDialog(root);
                    folderSelect.addListener(Events.Hide,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent ce) {
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
                Validate.callTo(move())
                    .check(Validations.notEmpty(_parentFolder))
                    .stopIfInError()
                    .check(Validations.uniqueResourceName(
                        _parent.getId(), _targetName))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable move() {
        return new Runnable() {
            public void run() {
                new MoveResourceAction(_target.getId(), _parent.getId()){
                    /** {@inheritDoc} */
                    @Override protected void onNoContent(
                                                     final Response response) {
                        _ssm.move(_target, _parent, _ssm.treeSelection());
                        hide();
                    }
                }.execute();
            }
        };
    }
}
