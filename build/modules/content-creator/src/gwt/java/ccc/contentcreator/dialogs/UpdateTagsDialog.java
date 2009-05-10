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
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.Globals;

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
    private final ResourceSummaryModelData _resource;


    /**
     * Constructor.
     *
     * @param resource The resource which tags to update.
     */
    public UpdateTagsDialog(final ResourceSummaryModelData resource) {
        super(Globals.uiConstants().updateTags());
        setHeight(Globals.DEFAULT_MIN_HEIGHT);
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
                    new DisposingCallback(UpdateTagsDialog.this)); // FIXME: must call _resource.setTags(tags)!!!!!
            }
        };
    }
}
