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

import ccc.api.ID;
import ccc.api.MimeType;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.contentcreator.validation.Validator;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;


/**
 * Dialog for creating / updating templates.
 *
 * @author Civic Computing Ltd.
 */
public class EditTemplateDialog extends AbstractWizardDialog  {


    /** DEFAULT_WIDTH : int. */
    protected static final int DEFAULT_WIDTH = 640;
    /** DEFAULT_HEIGHT : int. */
    protected static final int DEFAULT_HEIGHT = 480;
    /** TEXT_AREA_HEIGHT : int. */
    protected static final int TEXT_AREA_HEIGHT = 250;


    private final FormPanel _first = new FormPanel();
    private final FormPanel _second = new FormPanel();
    private final FormPanel _third = new FormPanel();

    private final TextField<String> _templateTitle = new TextField<String>();
    private final TextField<String> _name = new TextField<String>();
    private final TextArea _description = new TextArea();
    private final TextArea _body = new TextArea();
    private final TextArea _definition = new TextArea();

    private ID _id;
    private ID _parentFolderId;
    private DialogMode _mode;
    private SingleSelectionModel _ssm;
    private TemplateDelta _model;
    private ResourceSummaryModelData _proxy;

    /**
     * Constructor.
     *
     * @param parentFolderId The id of the parent folder.
     * @param ssm
     */
    public EditTemplateDialog(final ID parentFolderId,
                              final SingleSelectionModel ssm) {
        super(Globals.uiConstants().editTemplate());
        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);

        _parentFolderId = parentFolderId;
        _ssm = ssm;

        _name.setId("name");
        _templateTitle.setId("title");
        _description.setId("description");

        populateFirstScreen();
        populateSecondScreen();
        populateThirdScreen();

        addCard(_first);
        addCard(_second);
        addCard(_third);

        _mode = DialogMode.CREATE;

        refresh();
    }

    /**
     * Constructor.
     *
     * @param model The template to update.
     * @param proxy
     * @param ssm
     */
    public EditTemplateDialog(final TemplateDelta model,
                              final ResourceSummaryModelData proxy,
                              final SingleSelectionModel ssm) {
        this(null, ssm);
        _mode = DialogMode.UPDATE;

        _model = model;
        _proxy = proxy;

        _id = proxy.getId();

        _name.setReadOnly(true);
        _name.disable();

        _body.setValue(_model.getBody());
        _definition.setValue(_model.getDefinition());
        _description.setValue(_model.getDescription());
        _templateTitle.setValue(_model.getTitle());
        _name.setValue(proxy.getName());
    }

    private void populateFirstScreen() {
        _first.setWidth("100%");
        _first.setBorders(false);
        _first.setBodyBorder(false);
        _first.setHeaderVisible(false);

        _name.setFieldLabel(_constants.name());
        _name.setAllowBlank(false);
        _name.setId(_constants.name());
        _first.add(_name, new FormData("95%"));

        _templateTitle.setFieldLabel(_constants.title());
        _templateTitle.setAllowBlank(false);
        _templateTitle.setId(_constants.title());
        _first.add(_templateTitle, new FormData("95%"));

        _description.setFieldLabel(_constants.description());
        _description.setAllowBlank(false);
        _description.setId(_constants.description());
        _first.add(_description, new FormData("95%"));
    }

    private void populateSecondScreen() {
        _second.setWidth("100%");
        _second.setBorders(false);
        _second.setBodyBorder(false);
        _second.setHeaderVisible(false);

        _definition.setFieldLabel("content xml");
        _definition.setAllowBlank(false);
        _definition.setId("content xml");
        _definition.setHeight(TEXT_AREA_HEIGHT);

        _second.add(_definition, new FormData("95%"));
    }

    private void populateThirdScreen() {
        _third.setWidth("100%");
        _third.setBorders(false);
        _third.setBodyBorder(false);
        _third.setHeaderVisible(false);

        _body.setFieldLabel(_constants.body());
        _body.setAllowBlank(false);
        _body.setId(_constants.body());
        _body.setHeight(TEXT_AREA_HEIGHT);

        _third.add(_body, new FormData("95%"));
    }

    private TemplateDelta model() {
        final TemplateDelta delta =
            new TemplateDelta(
                _templateTitle.getValue(),
                _description.getValue(),
                _body.getValue(),
                _definition.getValue(),
                MimeType.HTML
            );
        return delta;
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(createTemplates())
                    .check(Validations.notEmpty(_definition))
                    .check(Validations.notEmpty(_name))
                    .check(Validations.notValidResourceName(_name))
                    .check(Validations.notEmpty(_templateTitle))
                    .check(Validations.noBrackets(_templateTitle))
                    .check(Validations.notEmpty(_description))
                    .check(Validations.noBrackets(_description))
                    .check(Validations.notEmpty(_body))
                    .stopIfInError()
                    .check(Validations.notValidXML(_definition))
                    .check(uniqueTemplateName(_name))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Validator uniqueTemplateName(final TextField<String> name) {
        return new Validator() {
            public void validate(final Validate validate) {
                if (_mode == DialogMode.UPDATE) {
                    validate.next();
                } else {
                    queries().templateNameExists(
                        name.getValue(),
                        new ErrorReportingCallback<Boolean>(){
                            public void onSuccess(final Boolean nameExists) {
                                if (nameExists.booleanValue()) {
                                    validate.addMessage(
                                        "A template with name '"
                                        + name.getValue()
                                        + "' already exists in this folder."
                                    );
                                }
                                validate.next();
                            }});
                }
            }

        };
    }

    private Runnable createTemplates() {
        return new Runnable() {
            public void run() {
                final TemplateDelta delta = model();
                    switch (_mode) {
                        case CREATE:
                            commands().createTemplate(
                            _parentFolderId,
                            delta,
                            _name.getValue(),
                            new ErrorReportingCallback<ResourceSummary>(){
                                public void onSuccess(final ResourceSummary arg0) {
                                    _ssm.create(
                                        new ResourceSummaryModelData(arg0),
                                        _ssm.treeSelection());
                                    close();
                                }});
                            break;
                        case UPDATE:
                            commands().updateTemplate(
                                _id,
                                delta,
                                new ErrorReportingCallback<Void>(){
                                    public void onSuccess(final Void arg0) {
                                        _proxy.setTitle(delta.getTitle());
                                        _ssm.update(_proxy);
                                        close();
                                    }});
                            break;
                        default:
                            Globals.alert(constants().error());
                            break;
                    }
            }
        };
    }

}
