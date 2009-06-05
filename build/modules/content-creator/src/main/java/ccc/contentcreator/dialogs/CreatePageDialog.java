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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccc.api.ID;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.api.ResourceSummary;
import ccc.api.TemplateSummary;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.binding.TemplateSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.EditPagePanel;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.PageElement;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;


/**
 * Wizard dialog for page creation.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageDialog
    extends
        AbstractWizardDialog {


    /** NAME_COLUMN_WIDTH : int. */
    private static final int NAME_COLUMN_WIDTH = 200;

    private final UIConstants _uiConstants = GWT.create(UIConstants.class);

    private final ContentPanel _first = new ContentPanel();
    private final EditPagePanel _second = new EditPagePanel();
    private final ContentPanel _third = new ContentPanel();

    private final ListStore<TemplateSummaryModelData> _templatesStore =
        new ListStore<TemplateSummaryModelData>();
    private final Grid<TemplateSummaryModelData> _grid;

    private final ContentPanel _descriptionPanel =
        new ContentPanel(new RowLayout());
    private final ContentPanel _rightPanel = new ContentPanel(new RowLayout());

    private final SingleSelectionModel _ssm;
    private final ResourceSummaryModelData _parent;

    private final CheckBox _publish = new CheckBox();


    private final Text _description = new Text("");

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
        super(Globals.uiConstants().createPage());
        _ssm = ssm;
        _parent = parent;

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final ColumnConfig nameColumn = new ColumnConfig();
        nameColumn.setId(TemplateSummaryModelData.Property.NAME.name());
        nameColumn.setHeader(_uiConstants.name());
        nameColumn.setWidth(NAME_COLUMN_WIDTH);
        nameColumn.setRenderer(new IdRenderer());
        configs.add(nameColumn);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<TemplateSummaryModelData>(_templatesStore, cm);
        _grid.setLoadMask(true);
        _grid.setId("TemplateGrid");

        final Listener<GridEvent> listener =
            new Listener<GridEvent>() {
            public void handleEvent(final GridEvent ge) {
                final TemplateSummaryModelData template =
                    (TemplateSummaryModelData)
                        ge.grid.getSelectionModel().getSelectedItem();
                _second.createFields(template.getDefinition());
                _description.setText(template.getDescription());
            }
        };
        _grid.addListener(Events.RowClick, listener);

        _templatesStore.add(DataBinding.bindTemplateDelta(list));

        final BorderLayoutData westData =
            new BorderLayoutData(LayoutRegion.WEST, 205);
        westData.setMargins(new Margins(5, 0, 5, 5));

        final BorderLayoutData centerData =
            new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(5));

        _first.setLayout(new BorderLayout());
        _rightPanel.setHeaderVisible(true);
        _rightPanel.setHeading(_uiConstants.template());
        _rightPanel.add(createCheckbox());
        _rightPanel.add(_grid);
        _first.add(_rightPanel, westData);

        _descriptionPanel.setHeaderVisible(true);
        _descriptionPanel.setHeading(_uiConstants.description());
        _descriptionPanel.add(_description);
        _first.add(_descriptionPanel, centerData);

        _first.setBorders(false);
        _first.setBodyBorder(false);
        _first.setHeaderVisible(false);
        addCard(_first);

        _second.setScrollMode(Style.Scroll.AUTOY);
        addCard(_second);

        _third.setBorders(false);
        _third.setBodyBorder(false);
        _third.setLayout(new FormLayout());
        _third.setHeaderVisible(false);

        _publish.setId(_uiConstants.publish());
        _publish.setValue(Boolean.FALSE);
        _publish.setBoxLabel(_uiConstants.yes());
        _publish.setFieldLabel(_uiConstants.publish());
        _third.add(_publish);
        addCard(_third);

        refresh();
    }

    private CheckBox createCheckbox() {

        final CheckBox cb = new CheckBox();
        cb.setBoxLabel(_uiConstants.useDefaultTemplate());
        cb.setId(_uiConstants.useDefaultTemplate());
        queries().computeTemplate(_parent.getId(),
            new ErrorReportingCallback<TemplateSummary>(
                _constants.createPage()) {
            public void onSuccess(final TemplateSummary result) {
                if (result == null) {
                    cb.setValue(Boolean.FALSE);
                    cb.disable();
                    _grid.enable();
                    _description.setText("");
                } else {
                    cb.setValue(Boolean.TRUE);
                    _grid.disable();
                    _grid.getSelectionModel().deselectAll();
                    _second.createFields(result.getDefinition());
                    _description.setText(result.getDescription());
                }
            }
        });

        cb.addListener(Events.Change, new Listener<FieldEvent>() {
            public void handleEvent(final FieldEvent be) {
                if (cb.getValue().booleanValue()) {
                    queries().computeTemplate(
                        _parent.getId(),
                        new ErrorReportingCallback<TemplateSummary>(
                            _constants.createPage()) {
                        public void onSuccess(final TemplateSummary result) {
                            if (result == null) {
                                cb.disable();
                                _grid.enable();
                                _description.setText("");
                            } else {
                                _grid.disable();
                                _grid.getSelectionModel().deselectAll();
                                _second.createFields(result.getDefinition());
                                _description.setText(result.getDescription());
                            }
                        }
                    });
                } else {
                    _second.removeAll();
                    _grid.enable();
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
                final List<PageElement> definitions =_second.pageElements();
                final Set<Paragraph> paragraphs =
                    new HashSet<Paragraph>();

                _second.extractValues(definitions, paragraphs);

                Validate.callTo(createPage(paragraphs))
                    .check(Validations.notEmpty(_second.name()))
                    .check(Validations.notValidResourceName(_second.name()))
                    .check(Validations.notEmpty(_second.title()))
                    .check(Validations.noBrackets(_second.title()))
                    .stopIfInError()
                    .check(Validations.uniqueResourceName(
                        _parent, _second.name()))
                    .stopIfInError()
                    .check(Validations.validateFields(paragraphs,
                        _second.definition()))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable createPage(final Set<Paragraph> paragraphs) {
        return new Runnable() {
            public void run() {
                final PageDelta page =
                    new PageDelta(
                        _second.title().getValue(),
                        paragraphs
                    );

                final ID template =
                    (null==_grid.getSelectionModel().getSelectedItem())
                    ? null
                    : _grid.getSelectionModel()
                           .getSelectedItem()
                           .getId();

                commands().createPage(
                    _parent.getId(),
                    page,
                    _second.name().getValue(),
                    _publish.getValue().booleanValue(),
                    template,
                    new ErrorReportingCallback<ResourceSummary>(
                        _constants.createPage()) {
                        public void onSuccess(final ResourceSummary result) {
                            _ssm.create(
                                new ResourceSummaryModelData(result),
                                _parent);
                            close();
                        }
                    }
                );
            }
        };
    }


    /**
     * Renderer for adding resource IDs to the grid.
     *
     * @author Civic Computing Ltd.
     */
    private static final class IdRenderer
        implements
            GridCellRenderer<TemplateSummaryModelData> {

        public String render(
                         final TemplateSummaryModelData model,
                         final String property,
                         final ColumnData config,
                         final int rowIndex,
                         final int colIndex,
                         final ListStore<TemplateSummaryModelData> store) {

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
}
