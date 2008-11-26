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
package ccc.contentcreator.client.dialogs;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.client.Validate;
import ccc.contentcreator.client.Validations;
import ccc.contentcreator.dto.ResourceDTO;

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

    private final ResourceDTO _item;
    private final TextField<String> _oldName = new TextField<String>();
    private final TextField<String> _newName = new TextField<String>();
    private ResourceTable _rt;

    /**
     * Constructor.
     *
     * @param item The resource to rename.
     * @param rt The ResourceTable to refresh.
     */
    public RenameDialog(final ResourceDTO item, final ResourceTable rt) {
        super(Globals.uiConstants().rename());
        _item = item;
        _rt = rt;

        setLayout(new FitLayout());
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
                        _item.getId(),
                        _newName))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable rename() {
        return new Runnable() {
            public void run() {
                Globals.resourceService().rename(
                    _item.getId(),
                    _newName.getValue(),
                    new ErrorReportingCallback<Void>() {
                        public void onSuccess(final Void result) {
                            _rt.refreshTable();
                            close();
                        }
                    });
            }
        };
    }

}
