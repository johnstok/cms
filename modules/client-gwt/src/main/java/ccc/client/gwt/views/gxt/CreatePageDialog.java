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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccc.api.core.Template;
import ccc.api.types.Paragraph;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.binding.TemplateSummaryModelData;
import ccc.client.gwt.core.Editable;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.ValidationResult;
import ccc.client.gwt.remoting.ComputeTemplateAction;
import ccc.client.gwt.views.CreatePage;
import ccc.client.gwt.widgets.EditPagePanel;
import ccc.client.gwt.widgets.PageElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SortDir;
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
import com.extjs.gxt.ui.client.widget.form.TextField;
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

    private Editable _presenter;

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
    private EditPagePanel _secondWizardPage = new EditPagePanel();
    private final ContentPanel _thirdWizardPage = new ContentPanel();

    private final ListStore<TemplateSummaryModelData> _templatesStore =
        new ListStore<TemplateSummaryModelData>();
    private final Grid<TemplateSummaryModelData> _templateGrid;

    private final ContentPanel _descriptionPanel =
        new ContentPanel(new RowLayout());
    private final ContentPanel _templatePanel =
        new ContentPanel(new RowLayout());


    private final Text _description = new Text("");
    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();

    private final ResourceSummaryModelData _parent;

    /**
     * Constructor.
     * @param templates
     * @param item
     *
     * @param list List of templates.
     * @param parent The Folder in which page will created.
     */
    public CreatePageDialog(final Collection<Template> list,
                            final ResourceSummaryModelData parent) {
        super(new GlobalsImpl().uiConstants().createPage(),
              new GlobalsImpl());
        _parent = parent;

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig templateNameColumn = new ColumnConfig();
        templateNameColumn.setId(TemplateSummaryModelData.Property.NAME.name());
        templateNameColumn.setHeader(getUiConstants().name());
        templateNameColumn.setWidth(NAME_COLUMN_WIDTH);
        configs.add(templateNameColumn);

        final ColumnModel cm = new ColumnModel(configs);

        _templateGrid = new Grid<TemplateSummaryModelData>(_templatesStore, cm);
        _templateGrid.setId("TemplateGrid");
        _templateGrid.setHeight(TEMPLATE_GRID_HEIGHT);

        final Listener<GridEvent<?>> gridEventlistener =
            new Listener<GridEvent<?>>() {
            public void handleEvent(final GridEvent<?> gridEvent) {
                final TemplateSummaryModelData t =
                    (TemplateSummaryModelData)
                        gridEvent.getGrid().getSelectionModel().getSelectedItem();
                updateSecondPage(t.getDefinition(), t.getDescription());
            }
        };
        _templateGrid.addListener(Events.RowClick, gridEventlistener);

        _templatesStore.add(DataBinding.bindTemplateDelta(list));
        _templatesStore.sort(
            TemplateSummaryModelData.Property.NAME.name(), SortDir.ASC);

        _templatePanel.setHeaderVisible(true);
        _templatePanel.setHeading(getUiConstants().template());
        _templatePanel.add(new DefaultCheckBox());
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

        _secondWizardPage.setScrollMode(Style.Scroll.AUTOY);
        addCard(_secondWizardPage);

        _thirdWizardPage.setBorders(false);
        _thirdWizardPage.setBodyBorder(false);
        _thirdWizardPage.setLayout(new FormLayout());
        _thirdWizardPage.setHeaderVisible(false);

        _majorEdit.setName("majorEdit");
        _majorEdit.setValue(Boolean.TRUE);
        _majorEdit.setBoxLabel(getUiConstants().yes());
        _majorEdit.setFieldLabel(getUiConstants().majorEdit());

        _comment.setFieldLabel(getUiConstants().comment());
        _comment.setName("comment");

        _thirdWizardPage.add(_majorEdit);
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
        private Template _t2 = null;

        DefaultCheckBox() {
            setBoxLabel(getUiConstants().useDefaultTemplate());
            setId(getUiConstants().useDefaultTemplate());

            new ComputeTemplateAction(
                getUiConstants().createPage(), _parent.getId()) {

                /** {@inheritDoc} */
                @Override
                protected void noTemplate() {
                    setValue(Boolean.FALSE);
                    disable();
                    _templateGrid.enable();
                    _description.setText("");
                }

                /** {@inheritDoc} */
                @Override protected void template(final Template t) {
                    _t2 = t;
                    setValue(Boolean.TRUE);
                    _templateGrid.disable();
                    _templateGrid.getSelectionModel().deselectAll();
                    updateSecondPage(t.getDefinition(), t.getDescription());
                }

            }.execute();

            addListener(Events.Change, new Listener<FieldEvent>() {
                public void handleEvent(final FieldEvent be) {
                    if (getValue().booleanValue()) {
                        if (null != _t2) {
                            _templateGrid.disable();
                            _templateGrid.getSelectionModel().deselectAll();
                            updateSecondPage(
                                _t2.getDefinition(), _t2.getDescription());
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

    private void updateSecondPage(final String definition,
                                  final String description) {
        final EditPagePanel second = new EditPagePanel();
        second.createFields(definition);
        second.setScrollMode(Style.Scroll.AUTOY);
        replaceCard(_secondWizardPage, second);
        _secondWizardPage = second;
        _description.setText(description);
        refresh();
    }

    /** {@inheritDoc} */
    @Override
    public void alert(final String message) { getGlobals().alert(message); }

    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        super.show();
    }

    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult result = new ValidationResult();
        return result;
    }

    /**
     * Accessor.
     *
     * @return Returns the presenter.
     */
    Editable getPresenter() {
        return _presenter;
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummaryModelData getSelectedTemplate() {
        return _templateGrid.getSelectionModel().getSelectedItem();
    }

    /** {@inheritDoc} */
    @Override
    public String getDefinition() {
        return _secondWizardPage.definition();
    }

    /** {@inheritDoc} */
    @Override
    public TextField<String> getName() {
        return _secondWizardPage.name();
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
    public Set<Paragraph> getParagraphs() {

        final List<PageElement> definitions =
            _secondWizardPage.pageElements();

        final Set<Paragraph> paragraphs =
            new HashSet<Paragraph>();
        _secondWizardPage.extractValues(definitions, paragraphs);
        return paragraphs;
    }

}
