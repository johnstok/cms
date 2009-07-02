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

import ccc.api.ResourceSummary;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * Dialog for folder creation.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFolderDialog extends AbstractEditDialog {

    private final ResourceSummaryModelData _parent;
    private final TextField<String> _text = new TextField<String>();
    private final SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param parent parent folder in the GUI.
     * @param ssm The selection model.
     */
    public CreateFolderDialog(final ResourceSummaryModelData parent,
                              final SingleSelectionModel ssm) {
        super(new IGlobalsImpl().uiConstants().createFolder());

        _ssm = ssm;

        setHeight(IGlobals.DEFAULT_MIN_HEIGHT);
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
            public void run() {
                commands().createFolder(
                    _parent.getId(),
                    _text.getValue(),
                    new ErrorReportingCallback<ResourceSummary>(
                        _constants.createFolder()){
                        public void onSuccess(final ResourceSummary result) {
                            final ResourceSummaryModelData newFolder =
                                new ResourceSummaryModelData(result);
                            _ssm.create(newFolder, _parent);
                            close();
                        }
                    }
                );
            }
        };
    }

}
