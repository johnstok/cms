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

import static ccc.services.api.ParagraphDelta.Type.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.binding.TemplateSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.EditPagePanel;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.PageElement;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.client.ui.FCKEditor;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;

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
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
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
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageDialog
    extends
        AbstractWizardDialog {

    private final UIConstants _uiConstants = GWT.create(UIConstants.class);

    private final ContentPanel _first = new ContentPanel();
    private final EditPagePanel _second = new EditPagePanel();
    private final ContentPanel _third = new ContentPanel();

    private ListStore<TemplateSummaryModelData> _templatesStore =
        new ListStore<TemplateSummaryModelData>();
    private Grid<TemplateSummaryModelData> _grid;

    private ContentPanel _descriptionPanel = new ContentPanel(new RowLayout());
    private ContentPanel _rightPanel = new ContentPanel(new RowLayout());

    private final SingleSelectionModel<ResourceSummaryModelData> _ssm;
    private final ResourceSummaryModelData _parent;

    private final CheckBox _publish = new CheckBox();


    private Text _description = new Text("");

    /**
     * Constructor.
     *
     * @param list List of templates.
     * @param parent The Folder in which page will created.
     * @param ssm SingleSelectionModel to update.
     */
    public CreatePageDialog(
                    final Collection<TemplateDelta> list,
                    final ResourceSummaryModelData parent,
                    final SingleSelectionModel<ResourceSummaryModelData> ssm) {
        super(Globals.uiConstants().createPage());
        _ssm = ssm;
        _parent = parent;

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final ColumnConfig nameColumn = new ColumnConfig();
        nameColumn.setId(_uiConstants.name());
        nameColumn.setHeader(_uiConstants.name());
        nameColumn.setWidth(200);
        nameColumn.setRenderer(createIdRenderer());
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

        _second.setScrollMode(Style.Scroll.ALWAYS);
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
        queries().getTemplateForResource(_parent.getId().toString(),
            new ErrorReportingCallback<TemplateDelta>() {
            public void onSuccess(final TemplateDelta result) {
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
                    queries().getTemplateForResource(_parent.getId().toString(),
                        new ErrorReportingCallback<TemplateDelta>() {
                        public void onSuccess(final TemplateDelta result) {
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

    private GridCellRenderer<TemplateSummaryModelData> createIdRenderer() {

        return new GridCellRenderer<TemplateSummaryModelData>() {
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
        };
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final List<PageElement> definitions =_second.pageElements();
                final List<ParagraphDelta> paragraphs =
                    new ArrayList<ParagraphDelta>();

                for (final PageElement c : definitions) {
                    if ("TEXT".equals(c.type())) {
                        final Field<String> f = c.field();
                        final ParagraphDelta p =
                            new ParagraphDelta(
                                c.id(),
                                TEXT,
                                null,
                                f.getValue(),
                                null,
                                null);
                        paragraphs.add(p);
                    } else if ("DATE".equals(c.type())) {
                        final DateField f = c.dateField();
                        final ParagraphDelta p =
                            new ParagraphDelta(
                                c.id(),
                                DATE,
                                f.getRawValue(),
                                null,
                                f.getValue(),
                                null);
                        paragraphs.add(p);
                    } else if ("HTML".equals(c.type())) {
                        final FCKEditor f = c.editor();
                        final ParagraphDelta p =
                            new ParagraphDelta(
                                c.id(),
                                TEXT,
                                null,
                                f.getHTML(),
                                null,
                                null);
                        paragraphs.add(p);
                    }
                }


                Validate.callTo(createPage(paragraphs))
                    .check(Validations.notEmpty(_second.name()))
                    .check(Validations.notValidResourceName(_second.name()))
                    .check(Validations.notEmpty(_second.title()))
                    .check(Validations.noBrackets(_second.title()))
                    .stopIfInError()
                    .check(Validations.uniqueResourceName(
                        _parent, _second.name()))
                    .check(Validations.validateDatefields(paragraphs))
                    .stopIfInError()
                    .check(Validations.validateFields(paragraphs,
                        _second.definition()))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable createPage(final List<ParagraphDelta> paragraphs) {
        return new Runnable() {
            public void run() {
                final PageDelta page =
                    new PageDelta(
                        null,
                        _second.name().getValue(),
                        _second.title().getValue(),
                        null,
                        "",
                        _publish.getValue().booleanValue(),
                        paragraphs,
                        null
                    );

                final String template =
                    (null==_grid.getSelectionModel().getSelectedItem())
                    ? null
                    : _grid.getSelectionModel()
                           .getSelectedItem()
                           .getId().toString();

                commands().createPage(
                    _parent.getId().toString(),
                    page,
                    template,
                    new ErrorReportingCallback<ResourceSummary>() {
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
}
