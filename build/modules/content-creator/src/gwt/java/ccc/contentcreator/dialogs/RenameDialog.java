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

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class RenameDialog extends AbstractEditDialog {

    private final ModelData _item;
    private final TextField<String> _oldName = new TextField<String>();
    private final TextField<String> _newName = new TextField<String>();
    private ResourceTable _rt;

    /**
     * Constructor.
     *
     * @param item The resource to rename.
     * @param rt The ResourceTable to refresh.
     */
    public RenameDialog(final ModelData item, final ResourceTable rt) {
        super(Globals.uiConstants().rename());
        _item = item;
        _rt = rt;

        setLayout(new FitLayout());
        setPanelId("RenamePanel");

        _oldName.setFieldLabel(constants().originalName());
        _oldName.setId(constants().originalName());
        _oldName.setReadOnly(true);
        _oldName.disable();
        _oldName.setValue(_item.<String>get("name"));

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
                        _item.<String>get("parentId"),
                        _newName))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable rename() {
        return new Runnable() {
            public void run() {
                commands().rename(
                    _item.<String>get("id"),
                    _item.<Long>get("version"),
                    _newName.getValue(),
                    new ErrorReportingCallback<Void>() {
                        public void onSuccess(final Void result) {
                            _rt.refreshTable(); // TODO: update model data
                            close();
                        }
                    });
            }
        };
    }

}
