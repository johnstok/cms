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
import ccc.contentcreator.client.IGlobals;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for updating resource's tags.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTagsDialog
    extends
        AbstractEditDialog {

    private final TextField<String> _tags = new TextField<String>();
    private final ResourceSummaryModelData _resource;


    /**
     * Constructor.
     *
     * @param resource The resource which tags to update.
     */
    public UpdateTagsDialog(final ResourceSummaryModelData resource) {
        super(Globals.uiConstants().updateTags());
        setHeight(IGlobals.DEFAULT_MIN_HEIGHT);
        _resource = resource;

        addTextField(_resource.getTags(),
                     "tags",
                     true,
                     constants().tags());
    }


    private void addTextField(final String value,
                              final String id,
                              final boolean allowBlank,
                              final String label) {
        _tags.setFieldLabel(label);
        _tags.setAllowBlank(allowBlank);
        _tags.setId(id);
        _tags.setValue(value);
        addField(_tags);
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final String tags =
                    (null==_tags.getValue()) ? "" : _tags.getValue();

                commands().updateTags(
                    _resource.getId(),
                    tags,
                    new ErrorReportingCallback<Void>(_constants.updateTags()){
                        @Override public void onSuccess(final Void arg0) {
                            _resource.setTags(tags);
                            UpdateTagsDialog.this.close();
                        }
                    }
                );
            }
        };
    }
}
