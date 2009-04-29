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

import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.data.BaseModelData;
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
    final private SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param parent parent folder in the GUI.
     */
    public CreateFolderDialog(final ModelData parent,
                              final SingleSelectionModel ssm) {
        super(Globals.uiConstants().createFolder());

        _ssm = ssm;

        setHeight(Globals.DEFAULT_MIN_HEIGHT);
        _parent = parent;
        setLayout(new FitLayout());
        setPanelId("create-folder-dialog");

        _text.setId("folder-name");
        _text.setFieldLabel(constants().name());
        _text.setEmptyText(constants().theFolderName());
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
                            final ModelData newFolder = new BaseModelData();
                            DataBinding.merge(newFolder, result);
                            _ssm.create(newFolder, _parent);
                            close();
                        }
                    }
                );
            }
        };
    }

}
