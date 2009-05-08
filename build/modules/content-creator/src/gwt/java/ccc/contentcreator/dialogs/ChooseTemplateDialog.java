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
import ccc.contentcreator.binding.TemplateSummaryModelData;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.Globals;
import ccc.services.api.ID;
import ccc.services.api.TemplateDelta;

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

    private final String _templateId;
    private final String _resourceId;
    private final Collection<TemplateDelta> _templates;

    private final TemplateSummaryModelData _none =
        new TemplateSummaryModelData(
            new TemplateDelta(
                null,
                "{none}",
                "{none}",
                "{none}",
                "{none}",
                "{none}"
            )
        );
    private final ComboBox<TemplateSummaryModelData> _selectedTemplate =
        new ComboBox<TemplateSummaryModelData>();

    /**
     * Constructor.
     *
     * @param templateId The currently selected template.
     * @param templates The available templates.
     */
    public ChooseTemplateDialog(final String resourceId,
                                final String templateId,
                                final Collection<TemplateDelta> templates) {
        super(Globals.uiConstants().chooseTemplate());
        setHeight(Globals.DEFAULT_MIN_HEIGHT);

        _resourceId = resourceId;
        _templateId = templateId;
        _templates = templates;

        _selectedTemplate.setFieldLabel(constants().defaultTemplate());
        _selectedTemplate.setTemplate("<tpl for=\".\">"
            +"<div class=x-combo-list-item id={NAME}>{NAME}</div></tpl>");
        _selectedTemplate.setId("default-template");
        _selectedTemplate.setDisplayField(TemplateSummaryModelData.Property.NAME.name());
        _selectedTemplate.setForceSelection(true);
        _selectedTemplate.setEditable(false);
        addField(_selectedTemplate);

        drawGUI();
    }

    private void drawGUI() {

        // Populate combo-box
        final ListStore<TemplateSummaryModelData> store =
            new ListStore<TemplateSummaryModelData>();
        store.add(DataBinding.bindTemplateDelta(_templates));
        store.add(_none);
        _selectedTemplate.setStore(store);

        // Set the current value
        if (null == _templateId) {
            _selectedTemplate.setValue(_none);
        } else {
            for (final TemplateSummaryModelData model : store.getModels()) {
                if (_templateId.equals(model.getId().toString())) {
                    _selectedTemplate.setValue(model);
                }
            }
        }
    }


    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override public void componentSelected(final ButtonEvent ce) {

                final TemplateSummaryModelData selected =
                    _selectedTemplate.getValue();
                final ID templateId = selected.getId();

                commands().updateResourceTemplate(
                    _resourceId,
                    templateId.toString(),
                    new DisposingCallback(ChooseTemplateDialog.this));
            }
        };
    }
}
