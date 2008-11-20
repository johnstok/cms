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

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.Validate;
import ccc.contentcreator.client.Validations;
import ccc.contentcreator.dto.FolderDTO;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFolderDialog extends AbstractEditDialog {

    private FolderDTO _item;
    private final ResourceServiceAsync _resourceService =
        Globals.resourceService();
    private TreeStore<FolderDTO> _treeStore;
    private final TextField<String> _text = new TextField<String>();

    /**
     * Constructor.
     *
     * @param item FolderDTO for parent folder.
     * @param treeStore TreeStore to update.
     */
    public CreateFolderDialog(final FolderDTO item,
                              final TreeStore<FolderDTO> treeStore) {
        super(Globals.uiConstants().createFolder());
        _item = item;
        _treeStore = treeStore;
        setLayout(new FitLayout());
        setPanelId("create-folder-dialog");

        _text.setId("folder-name");
        _text.setFieldLabel(constants().name());
        _text.setEmptyText("The folder name");
        _text.setAllowBlank(false);
        addField(_text);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(createFolder())
                    .check(Validations.notEmpty(_text))
                    .check(Validations.notValidResourceName(_text))
                    .stopIfInError()
                    .check(Validations.uniqueResourceName(_item, _text))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable createFolder() {
        return new Runnable() {
            @SuppressWarnings("unchecked")
            public void run() {
                _resourceService.createFolder(
                    _item,
                    _text.getValue(),
                    new ErrorReportingCallback<FolderDTO>(){
                        public void onSuccess(final FolderDTO result) {
                            fireEvent(Events.SelectionChange);
                            _treeStore.add(_item, result, false);
                            close();
                        }
                    }
                );
            }
        };
    }

}
