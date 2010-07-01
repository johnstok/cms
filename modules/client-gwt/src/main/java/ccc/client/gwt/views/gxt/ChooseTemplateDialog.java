/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import java.util.Collection;
import java.util.UUID;

import ccc.api.core.Template;
import ccc.api.types.ResourceName;
import ccc.client.core.Editable;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.views.ChangeResourceTemplate;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;


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

    private final ComboBox<BeanModel> _selectedTemplate =
        new ComboBox<BeanModel>();
    private final ListStore<BeanModel> _store =
        new ListStore<BeanModel>();

    private final BeanModel _none =
        DataBinding.bindTemplate(
            Template.summary(
                null,
                new ResourceName("NONE"),
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
        super(I18n.UI_CONSTANTS.chooseTemplate(),
              new GlobalsImpl());
        setHeight(Globals.DEFAULT_MIN_HEIGHT);

        _selectedTemplate.setFieldLabel(constants().defaultTemplate());
//        _selectedTemplate.setTemplate("<tpl for=\".\">"
//            +"<div class=x-combo-list-item id={NAME}>{NAME}</div></tpl>");
//        _selectedTemplate.setId("default-template");
        _selectedTemplate.setDisplayField(
            DataBinding.TemplateBeanModel.NAME);
        _selectedTemplate.setForceSelection(true);
        _selectedTemplate.setEditable(false);
        _selectedTemplate.setTriggerAction(TriggerAction.ALL);
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
        return _selectedTemplate.getValue().<Template>getBean().getId();
    }


    /** {@inheritDoc} */
    @Override
    public void setSelectedTemplate(final UUID templateId) {
        if (null == templateId) {
            _selectedTemplate.setValue(_none);
        } else {
            for (final BeanModel model : _store.getModels()) {
                if (templateId.equals(model.<Template>getBean().getId())) {
                    _selectedTemplate.setValue(model);
                    break;
                }
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    public void setTemplates(final Collection<Template> templates) {
        _store.add(_none);
        _store.add(DataBinding.bindTemplateDelta(templates));
        _store.sort(DataBinding.TemplateBeanModel.NAME, SortDir.ASC);
        _selectedTemplate.setStore(_store);
    }


    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        super.show();
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
