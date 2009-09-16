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
package ccc.contentcreator.views.gxt;

import java.util.Collection;
import java.util.UUID;

import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.TemplateSummaryModelData;
import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.dialogs.AbstractEditDialog;
import ccc.contentcreator.views.ChangeResourceTemplate;
import ccc.rest.dto.TemplateSummary;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;


/**
 * Dialog for resource template chooser.
 *
 * @author Civic Computing Ltd
 */
public class ChooseTemplateDialog
    extends
        AbstractEditDialog
    implements
        ChangeResourceTemplate {

    private Editable _presenter;

    private final ComboBox<TemplateSummaryModelData> _selectedTemplate =
        new ComboBox<TemplateSummaryModelData>();
    private final ListStore<TemplateSummaryModelData> _store =
        new ListStore<TemplateSummaryModelData>();

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


    /**
     * Constructor.
     */
    public ChooseTemplateDialog() {
        super(new IGlobalsImpl().uiConstants().chooseTemplate(),
              new IGlobalsImpl());
        setHeight(IGlobals.DEFAULT_MIN_HEIGHT);

        _selectedTemplate.setFieldLabel(constants().defaultTemplate());
        _selectedTemplate.setTemplate("<tpl for=\".\">"
            +"<div class=x-combo-list-item id={NAME}>{NAME}</div></tpl>");
        _selectedTemplate.setId("default-template");
        _selectedTemplate.setDisplayField(
            TemplateSummaryModelData.Property.NAME.name());
        _selectedTemplate.setForceSelection(true);
        _selectedTemplate.setEditable(false);
        addField(_selectedTemplate);
    }


    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override public void componentSelected(final ButtonEvent ce) {
                getPresenter().save();
            }
        };
    }


    /** {@inheritDoc} */
    @Override
    public UUID getSelectedTemplate() {
        return _selectedTemplate.getValue().getId();
    }


    /** {@inheritDoc} */
    @Override
    public void setSelectedTemplate(final UUID templateId) {
        if (null == templateId) {
            _selectedTemplate.setValue(_none);
        } else {
            for (final TemplateSummaryModelData model : _store.getModels()) {
                if (templateId.equals(model.getId())) {
                    _selectedTemplate.setValue(model);
                    break;
                }
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    public void setTemplates(final Collection<TemplateSummary> templates) {
        _store.add(DataBinding.bindTemplateDelta(templates));
        _store.add(_none);
        _selectedTemplate.setStore(_store);
    }


    /** {@inheritDoc} */
    @Override
    public void setPresenter(final Editable presenter) {
        _presenter = presenter;
    }


    /**
     * Accessor.
     *
     * @return Returns the presenter.
     */
    Editable getPresenter() {
        return _presenter;
    }
}
