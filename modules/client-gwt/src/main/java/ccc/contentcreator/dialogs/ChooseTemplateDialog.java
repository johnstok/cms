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

import ccc.contentcreator.actions.UpdateResourceTemplateAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.binding.TemplateSummaryModelData;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.rest.TemplateSummary;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.http.client.Response;


/**
 * Dialog for resource template chooser.
 *
 * @author Civic Computing Ltd
 */
public class ChooseTemplateDialog extends AbstractEditDialog {

    private final ResourceSummaryModelData    _resource;
    private final Collection<TemplateSummary> _templates;
    private final SingleSelectionModel        _ssm;

    private final TemplateSummaryModelData _none =
        new TemplateSummaryModelData(
            new TemplateSummary(
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
     * @param resource The resource to update.
     * @param templates The available templates.
     * @param ssm The selection model.
     */
    public ChooseTemplateDialog(final ResourceSummaryModelData resource,
                                final Collection<TemplateSummary> templates,
                                final SingleSelectionModel ssm) {
        super(new IGlobalsImpl().uiConstants().chooseTemplate(),
              new IGlobalsImpl());
        setHeight(IGlobals.DEFAULT_MIN_HEIGHT);

        _resource = resource;
        _templates = templates;
        _ssm = ssm;

        _selectedTemplate.setFieldLabel(constants().defaultTemplate());
        _selectedTemplate.setTemplate("<tpl for=\".\">"
            +"<div class=x-combo-list-item id={NAME}>{NAME}</div></tpl>");
        _selectedTemplate.setId("default-template");
        _selectedTemplate.setDisplayField(
            TemplateSummaryModelData.Property.NAME.name());
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
        if (null == _resource.getTemplateId()) {
            _selectedTemplate.setValue(_none);
        } else {
            for (final TemplateSummaryModelData model : store.getModels()) {
                if (_resource.getTemplateId().equals(model.getId())) {
                    _selectedTemplate.setValue(model);
                    break;
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

                new UpdateResourceTemplateAction(
                    _resource.getId(), selected.getId()) {
                        /** {@inheritDoc} */
                        @Override protected void onNoContent(final Response r) {
                            ChooseTemplateDialog.this.hide();
                            _resource.setTemplateId(selected.getId());
                            _ssm.update(_resource);
                        }
                }.execute();
            }
        };
    }
}
