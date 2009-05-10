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

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for renaming a resource.
 *
 * @author Civic Computing Ltd.
 */
public class RenameDialog extends AbstractEditDialog {

    private final ResourceSummaryModelData _item;
    private final TextField<String> _oldName = new TextField<String>();
    private final TextField<String> _newName = new TextField<String>();
    private SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param item The resource to rename.
     * @param ssm The selection model.
     */
    public RenameDialog(
                    final ResourceSummaryModelData item,
                    final SingleSelectionModel ssm) {
        super(Globals.uiConstants().rename());
        setHeight(Globals.DEFAULT_MIN_HEIGHT);
        _item = item;
        _ssm = ssm;

        setPanelId("RenamePanel");

        _oldName.setFieldLabel(constants().originalName());
        _oldName.setId(constants().originalName());
        _oldName.setReadOnly(true);
        _oldName.disable();
        _oldName.setValue(_item.getName());

        _newName.setFieldLabel(constants().newName());
        _newName.setId(constants().newName());
        _newName.setAllowBlank(false);

        addField(_oldName);
        addField(_newName);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(rename())
                    .check(Validations.notEmpty(_newName))
                    .stopIfInError()
                    .check(Validations.notValidResourceName(_newName))
                    .stopIfInError()
                    .check(Validations.uniqueResourceName(
                        _item.getParent(),
                        _newName))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable rename() {
        return new Runnable() {
            public void run() {
                commands().rename(
                    _item.getId(),
                    _newName.getValue(),
                    new ErrorReportingCallback<Void>() {
                        public void onSuccess(final Void result) {
                            _item.setName(_newName.getValue());
                            _ssm.update(_item);
                            close();
                        }
                    });
            }
        };
    }

}
