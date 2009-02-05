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
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Events;
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
public class CreateFolderDialog extends AbstractEditDialog {

    private final ModelData _parent;
    private final TextField<String> _text = new TextField<String>();

    /**
     * Constructor.
     *
     * @param parent parent folder in the GUI.
     */
    public CreateFolderDialog(final ModelData parent) {
        super(Globals.uiConstants().createFolder());
        setHeight(Globals.DEFAULT_MIN_HEIGHT);
        _parent = parent;
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
                    .check(Validations.uniqueResourceName(_parent, _text))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable createFolder() {
        return new Runnable() {
            @SuppressWarnings("unchecked")
            public void run() {
                commands().createFolder(
                    _parent.<String>get("id"),
                    _text.getValue(),
                    new ErrorReportingCallback<ResourceSummary>(){
                        public void onSuccess(final ResourceSummary result) {
                            fireEvent(Events.SelectionChange);
//                            _treeStore.add(_parent,
//                                           DataBinding.bindResourceSummary(Collections.singletonList(result)),
//                                           false);
                            // TODO: Sync GUI.
                            close();
                        }
                    }
                );
            }
        };
    }

}
