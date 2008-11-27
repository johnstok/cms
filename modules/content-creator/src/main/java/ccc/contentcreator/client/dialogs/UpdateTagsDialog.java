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

import ccc.contentcreator.client.Globals;
import ccc.contentcreator.dto.ResourceDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTagsDialog
    extends
        AbstractEditDialog {

    private final TextField<String> _tags = new TextField<String>();
    private final ResourceDTO _resource;
    private final ListStore<ResourceDTO> _store;


    /**
     * Constructor.
     *
     * @param result
     * @param store
     */
    public UpdateTagsDialog(final ResourceDTO result,
                            final ListStore<ResourceDTO> store) {
        super(Globals.uiConstants().updateTags());
        _resource = result;
        _store = store;

        addTextField(_resource.<String>get("tags"),
                     "tags",
                     true,
                     constants().tags());
    }



    /**
     * TODO: Add a description of this method.
     *
     * @param value
     * @param id
     * @param allowBlank
     * @param label
     */
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

                resourceService().updateTags(_resource.getId(),
                                             tags,
                                             new AsyncCallback<Void>(){
                    public void onFailure(final Throwable caught) {
                        Globals.unexpectedError(caught);
                    }
                    public void onSuccess(final Void result) {
                        close();
                        _resource.set("tags", tags);
                        _store.update(_resource);
                    }});
            }
        };
    }
}
