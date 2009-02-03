/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import java.util.Collection;

import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.Globals;
import ccc.services.api.ResourceDelta;
import ccc.services.api.TemplateDelta;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;


/**
 * Dialog for resource template chooser. Mostly copied from
 * UpdateOptionsDialog
 *
 * @author Civic Computing Ltd
 */
public class ChooseTemplateDialog extends AbstractEditDialog {

    private ResourceDelta _resource;
    private Collection<TemplateDelta> _templates;
    private final ModelData _none = new BaseModelData();
    private final ComboBox<ModelData> _selectedTemplate =
        new ComboBox<ModelData>();

    /**
     * Constructor.
     *
     * @param resourceDelta A resource to which template would be assigned.
     * @param templates Collection of templates.
     */
    public ChooseTemplateDialog(final ResourceDelta resourceDelta,
                                final Collection<TemplateDelta> templates) {
        super(Globals.uiConstants().chooseTemplate());
        setHeight(Globals.DEFAULT_MIN_HEIGHT);
        _resource = resourceDelta;
        _templates = templates;
        _none.set("name", "{none}");

        _selectedTemplate.setFieldLabel(constants().defaultTemplate());
        _selectedTemplate.setTemplate("<tpl for=\".\">"
            +"<div class=x-combo-list-item id={name}>{name}</div></tpl>");
        _selectedTemplate.setId("default-template");
        _selectedTemplate.setDisplayField("name");
        _selectedTemplate.setForceSelection(true);
        _selectedTemplate.setEditable(false);
        addField(_selectedTemplate);

        drawGUI();
    }

    private void drawGUI() {

        // Populate combo-box
        final ListStore<ModelData> store = new ListStore<ModelData>();
        store.add(DataBinding.bindTemplateDelta(_templates));
        store.add(_none);
        _selectedTemplate.setStore(store);

        // Set the current value
        if (null == _resource._templateId) {
            _selectedTemplate.setValue(_none);
        } else {
            for (final ModelData model : store.getModels()) {
                if (_resource._templateId.equals(model.get("id"))) {
                    _selectedTemplate.setValue(model);
                }
            }
        }
    }


    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override public void componentSelected(final ButtonEvent ce) {

                final ModelData selected = _selectedTemplate.getValue();
                final String templateId = selected.get("id");

                commands().updateResourceTemplate(
                    _resource._id,
                    templateId,
                    new DisposingCallback(ChooseTemplateDialog.this));
            }
        };
    }
}
