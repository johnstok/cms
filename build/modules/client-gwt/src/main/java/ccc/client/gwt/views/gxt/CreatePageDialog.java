/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.types.Paragraph;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.widgets.EditPagePanel;
import ccc.client.presenters.CreatePagePresenter;
import ccc.client.views.CreatePage;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;


/**
 * Wizard dialog for page creation.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageDialog
    extends
        AbstractWizardDialog
    implements
        CreatePage {

    private CreatePagePresenter _presenter;

    private static final int SCROLLBAR_WIDTH = 20;
    private static final int DEFAULT_MARGIN = 5;
    private static final int LEFT_MARGIN = DEFAULT_MARGIN;
    private static final int BOTTOM_MARGIN = DEFAULT_MARGIN;
    private static final int RIGHT_MARGIN = 0;
    private static final int TOP_MARGIN = DEFAULT_MARGIN;

    /** TEMPLATE_GRID_HEIGHT : int. */
    private static final int TEMPLATE_GRID_HEIGHT = 340;

    /** NAME_COLUMN_WIDTH : int. */
    private static final int NAME_COLUMN_WIDTH = 260;

    private final ContentPanel _firstWizardPage = new ContentPanel();
    private EditPagePanel _secondWizardPage;
    private final ContentPanel _thirdWizardPage = new ContentPanel();

    private final ListStore<BeanModel> _templatesStore =
        new ListStore<BeanModel>();
    private final Grid<BeanModel> _templateGrid;

    private final ContentPanel _descriptionPanel =
        new ContentPanel(new RowLayout());
    private final ContentPanel _templatePanel =
        new ContentPanel(new RowLayout());
    private final CheckBox _useDefaultTemplate;

    private Template _template = null;
    private final Template _t2 = null;


    private final Text _description = new Text("");
    private final CheckBox _majorEdit = new CheckBox();
    private final CheckBox _publish = new CheckBox();
    private final TextArea _comment = new TextArea();
    private final ResourceSummary _targetRoot;


    /**
     * Constructor.
     * @param templates
     * @param item
     *
     * @param list List of templates.
     */
    public CreatePageDialog(final Collection<Template> list,
                            final ResourceSummary targetRoot) {
        super(I18n.uiConstants.createPage(), InternalServices.globals);
        _targetRoot = targetRoot;

        _secondWizardPage = new EditPagePanel(null, _targetRoot);

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig templateNameColumn = new ColumnConfig();
        templateNameColumn.setId(ResourceSummary.Properties.NAME);
        templateNameColumn.setHeader(getUiConstants().name());
        templateNameColumn.setWidth(NAME_COLUMN_WIDTH);
        configs.add(templateNameColumn);

        final ColumnModel cm = new ColumnModel(configs);

        _templateGrid = new Grid<BeanModel>(_templatesStore, cm);
        _templateGrid.setHeight(TEMPLATE_GRID_HEIGHT);

        final Listener<GridEvent<?>> gridEventlistener =
            new Listener<GridEvent<?>>() {
            public void handleEvent(final GridEvent<?> gridEvent) {
                final Template t =
                    ((BeanModel) gridEvent
                        .getGrid()
                        .getSelectionModel()
                        .getSelectedItem())
                        .getBean();
                updateSecondPage(t);
            }
        };
        _templateGrid.addListener(Events.RowClick, gridEventlistener);

        _templatesStore.add(DataBinding.bindTemplateDelta(list));
        _templatesStore.sort(ResourceSummary.Properties.NAME, SortDir.ASC);

        _templatePanel.setHeaderVisible(true);
        _templatePanel.setHeading(getUiConstants().template());
        _useDefaultTemplate = new DefaultCheckBox();
        _templatePanel.add(_useDefaultTemplate);
        _templatePanel.add(_templateGrid);

        final BorderLayoutData centerData =
            new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(DEFAULT_MARGIN));
        _descriptionPanel.setHeaderVisible(true);
        _descriptionPanel.setHeading(getUiConstants().description());
        _descriptionPanel.add(_description);

        final int templatePanelWidth = NAME_COLUMN_WIDTH + SCROLLBAR_WIDTH;
        final BorderLayoutData templateBorderLayoutData =
            new BorderLayoutData(LayoutRegion.WEST, templatePanelWidth);
        templateBorderLayoutData.setMargins(new Margins(TOP_MARGIN,
                                                   RIGHT_MARGIN,
                                                   BOTTOM_MARGIN,
                                                   LEFT_MARGIN));

        _firstWizardPage.setLayout(new BorderLayout());
        _firstWizardPage.setBorders(false);
        _firstWizardPage.setBodyBorder(false);
        _firstWizardPage.setHeaderVisible(false);

        _firstWizardPage.add(_templatePanel, templateBorderLayoutData);
        _firstWizardPage.add(_descriptionPanel, centerData);
        addCard(_firstWizardPage);

        addCard(_secondWizardPage);

        _thirdWizardPage.setBorders(false);
        _thirdWizardPage.setBodyBorder(false);
        _thirdWizardPage.setLayout(new FormLayout());
        _thirdWizardPage.setHeaderVisible(false);

        _majorEdit.setName("majorEdit");
        _majorEdit.setValue(Boolean.TRUE);
        _majorEdit.setBoxLabel(getUiConstants().yes());
        _majorEdit.setFieldLabel(getUiConstants().majorEdit());

        _publish.setName("majorEdit");
        _publish.setValue(Boolean.FALSE);
        _publish.setBoxLabel(getUiConstants().yes());
        _publish.setFieldLabel(getUiConstants().publish());

        _comment.setFieldLabel(getUiConstants().comment());
        _comment.setName("comment");

        _thirdWizardPage.add(_majorEdit);
        _thirdWizardPage.add(_publish);
        _thirdWizardPage.add(_comment);
        addCard(_thirdWizardPage);

        refresh();
    }

    /**
     * Checkbox for default template.
     *
     * @author Civic Computing Ltd.
     */
    private class DefaultCheckBox extends CheckBox {
        DefaultCheckBox() {
            setBoxLabel(getUiConstants().useDefaultTemplate());

            addListener(Events.Change, new Listener<FieldEvent>() {
                public void handleEvent(final FieldEvent be) {
                    if (getValue().booleanValue()) {
                        if (null != _t2) {
                            _templateGrid.disable();
                            _templateGrid.getSelectionModel().deselectAll();
                            updateSecondPage(_t2);
                        }
                    } else {
                        _secondWizardPage.removeAll();
                        _templateGrid.enable();
                        _description.setText("");
                    }
                }
            });
        }
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
               getPresenter().save();
            }
        };
    }

    private void updateSecondPage(final Template t) {
        _template = t;
        final EditPagePanel second =
            new EditPagePanel(t.getDefinition(), _targetRoot);
        replaceCard(_secondWizardPage, second);
        _secondWizardPage = second;
        _description.setText(t.getDescription());
        refresh();
    }

    /** {@inheritDoc} */
    @Override
    public void alert(final String message) {
        InternalServices.window.alert(message);
    }

    /** {@inheritDoc} */
    @Override
    public void show(final CreatePagePresenter presenter) {
        _presenter = presenter;

        final UUID t = getPresenter().getModel().getTemplate();

        if(null==t) {
            _useDefaultTemplate.setValue(Boolean.FALSE);
            _useDefaultTemplate.disable();
            _templateGrid.enable();
            _description.setText("");
        } else {
            final Template _t2 = findTemplate(t);
            _useDefaultTemplate.setValue(Boolean.TRUE);
            _templateGrid.disable();
            _templateGrid.getSelectionModel().deselectAll();
            updateSecondPage(_t2);
        }

        super.show();
    }


    private Template findTemplate(final UUID t) {
        return
            _templatesStore
            .findModel(ResourceSummary.Properties.UUID, t)
            .getBean();
    }

    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        return _secondWizardPage.getValidationResult();
    }

    /**
     * Accessor.
     *
     * @return Returns the presenter.
     */
    CreatePagePresenter getPresenter() {
        return _presenter;
    }

    /** {@inheritDoc} */
    @Override
    public Template getSelectedTemplate() {
        return _template;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return _secondWizardPage.getName().getValue();
    }


    /** {@inheritDoc} */
    @Override
    public String getComment() {
        return _comment.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public boolean getMajorEdit() {
        return _majorEdit.getValue().booleanValue();
    }

    /** {@inheritDoc} */
    @Override
    public boolean getPublish() {
        return _publish.getValue().booleanValue();
    }

    /** {@inheritDoc} */
    @Override
    public Set<Paragraph> getParagraphs() {
        return _secondWizardPage.getValues();
    }

    @Override
    public String getResourceTitle() {
        return _secondWizardPage.getResourceTitle().getValue();
    }

    @Override
    public UUID getUserID() {
        return InternalServices.globals.currentUser().getId();
    }

}
