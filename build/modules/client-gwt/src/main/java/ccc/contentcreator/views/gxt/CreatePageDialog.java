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
package ccc.contentcreator.views.gxt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.api.dto.PageDelta;
import ccc.api.dto.TemplateSummary;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.binding.TemplateSummaryModelData;
import ccc.contentcreator.core.GlobalsImpl;
import ccc.contentcreator.core.SingleSelectionModel;
import ccc.contentcreator.events.PageCreated;
import ccc.contentcreator.events.PageCreated.PageCreatedHandler;
import ccc.contentcreator.remoting.ComputeTemplateAction;
import ccc.contentcreator.remoting.CreatePageAction;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.contentcreator.widgets.EditPagePanel;
import ccc.contentcreator.widgets.PageElement;
import ccc.types.Paragraph;

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
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
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
        PageCreatedHandler {

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

    private final SingleSelectionModel _ssm;
    private final ResourceSummaryModelData _parent;

    private final Text _description = new Text("");
    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();

    /**
     * Constructor.
     *
     * @param list List of templates.
     * @param parent The Folder in which page will created.
     * @param ssm SingleSelectionModel to update.
     */
    public CreatePageDialog(
                    final Collection<TemplateSummary> list,
                    final ResourceSummaryModelData parent,
                    final SingleSelectionModel ssm) {
        super(new GlobalsImpl().uiConstants().createPage(),
              new GlobalsImpl());
        _ssm = ssm;
        _parent = parent;

        // FIXME Add event handler to hide(); dialog onSuccess();

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig templateNameColumn = new ColumnConfig();
        templateNameColumn.setId(TemplateSummaryModelData.Property.NAME.name());
        templateNameColumn.setHeader(getUiConstants().name());
        templateNameColumn.setWidth(NAME_COLUMN_WIDTH);
        templateNameColumn.setRenderer(new IdRenderer());
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
        _templatePanel.add(createCheckbox());
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

    private CheckBox createCheckbox() {

        final CheckBox cb = new CheckBox();
        cb.setBoxLabel(getUiConstants().useDefaultTemplate());
        cb.setId(getUiConstants().useDefaultTemplate());
        new ComputeTemplateAction(
            getUiConstants().createPage(), _parent.getId()) {

            /** {@inheritDoc} */
            @Override
            protected void noTemplate() {
                cb.setValue(Boolean.FALSE);
                cb.disable();
                _templateGrid.enable();
                _description.setText("");
            }

            /** {@inheritDoc} */
            @Override protected void template(final TemplateSummary t) {
                cb.setValue(Boolean.TRUE);
                _templateGrid.disable();
                _templateGrid.getSelectionModel().deselectAll();
                updateSecondPage(t.getDefinition(), t.getDescription());
            }

        }.execute();

        cb.addListener(Events.Change, new Listener<FieldEvent>() {
            public void handleEvent(final FieldEvent be) {
                if (cb.getValue().booleanValue()) {
                    new ComputeTemplateAction(getUiConstants().createPage(),
                                              _parent.getId()) {

                        /** {@inheritDoc} */
                        @Override
                        protected void noTemplate() {
                            cb.disable();
                            _templateGrid.enable();
                            _description.setText("");
                        }

                        /** {@inheritDoc} */
                        @Override protected void template(final TemplateSummary t) {
                            _templateGrid.disable();
                            _templateGrid.getSelectionModel().deselectAll();
                            updateSecondPage(
                                t.getDefinition(), t.getDescription());
                        }

                    }.execute();
                } else {
                    _secondWizardPage.removeAll();
                    _templateGrid.enable();
                    _description.setText("");
                }
            }
        });
        return cb;
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final List<PageElement> definitions =
                    _secondWizardPage.pageElements();
                final Set<Paragraph> paragraphs =
                    new HashSet<Paragraph>();

                _secondWizardPage.extractValues(definitions, paragraphs);

                Validate.callTo(createPage(paragraphs))
                    .check(Validations.notEmpty(_secondWizardPage.name()))
                    .stopIfInError()
                    .check(Validations.notValidResourceName(
                        _secondWizardPage.name()))
                    .stopIfInError()
                    .check(Validations.uniqueResourceName(
                        _parent, _secondWizardPage.name()))
                    .stopIfInError()
                    .check(Validations.validateFields(paragraphs,
                        _secondWizardPage.definition()))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable createPage(final Set<Paragraph> paragraphs) {
        return new Runnable() {
            public void run() {
                final PageDelta page = new PageDelta(paragraphs);
                final UUID template =
                    (null==_templateGrid.getSelectionModel().getSelectedItem())
                    ? null
                    : _templateGrid.getSelectionModel()
                           .getSelectedItem()
                           .getId();

                new CreatePageAction(
                    _parent.getId(),
                    page,
                    _secondWizardPage.name().getValue(),
                    template,
                    _secondWizardPage.name().getValue(), // Title
                    _comment.getValue(),
                    _majorEdit.getValue().booleanValue())
                .execute();
            }
        };
    }


    /**
     * Renderer for adding resource UUIDs to the grid.
     *
     * @author Civic Computing Ltd.
     */
    static final class IdRenderer
        implements
            GridCellRenderer<TemplateSummaryModelData> {

        /** {@inheritDoc} */
        @Override
        public Object render(final TemplateSummaryModelData model,
                             final String property,
                             final ColumnData config,
                             final int rowIndex,
                             final int colIndex,
                             final ListStore<TemplateSummaryModelData> store,
                             final Grid<TemplateSummaryModelData> grid) {

            String value = "";

            if (null != model) {
            final StringBuilder html = new StringBuilder();
            html.append("<div id='");
            // do not use id for id, otherwise acceptance tests fail.
            html.append(model.getName());
            html.append("'>");
            html.append(model.getName());
            html.append("</div>");
            value = html.toString();
            }
            return value;
        }
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
    @Override public void onCreate(final PageCreated event) { hide(); }
}
