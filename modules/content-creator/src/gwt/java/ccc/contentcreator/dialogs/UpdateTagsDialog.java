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

import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.Globals;
import ccc.services.api.ResourceDelta;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTagsDialog
    extends
        AbstractEditDialog {

    private final TextField<String> _tags = new TextField<String>();
    private final ResourceDelta _resource;


    /**
     * Constructor.
     *
     * @param resource The resource which tags to update.
     */
    public UpdateTagsDialog(final ResourceDelta resource) {
        super(Globals.uiConstants().updateTags());
        _resource = resource;

        addTextField(_resource._tags,
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
                    _resource._id,
                    tags,
                    new DisposingCallback(UpdateTagsDialog.this));
            }
        };
    }
}
