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

package ccc.contentcreator.client.dialogs;

import ccc.contentcreator.api.DialogMode;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.Validate;
import ccc.contentcreator.client.Validations;
import ccc.contentcreator.client.Validator;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;


/**
 * TODO Add Description for this type.
 * TODO Properly refresh object cache so GUI reflects changes.
 *
 * @author Civic Computing Ltd
 */
public class EditTemplateDialog extends AbstractWizardDialog  {

    private final ResourceServiceAsync _resourceService =
        Globals.resourceService();

    /** DEFAULT_WIDTH : int. */
    protected static final int DEFAULT_WIDTH = 640;
    /** DEFAULT_HEIGHT : int. */
    protected static final int DEFAULT_HEIGHT = 480;

    /** _constants : UIConstants. */
    private final UIConstants _constants = Globals.uiConstants();


    private final FormPanel _first = new FormPanel();
    private final FormPanel _second = new FormPanel();
    private final FormPanel _third = new FormPanel();

    private final TextField<String> _templateTitle = new TextField<String>();
    private final TextField<String> _name = new TextField<String>();
    private final TextArea _description = new TextArea();
    private final TextArea _body = new TextArea();
    private final TextArea _definition = new TextArea();

    private String _id;
    private DialogMode _mode;
    private ListStore<ResourceDTO> _store;
    private TemplateDTO _model;

    /**
     * Constructor.
     */
    public EditTemplateDialog() {
        super(Globals.uiConstants().editTemplate());
        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);

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
     * @param item TemplateDTO for the template.
     * @param store ListStore model for the dialog.
     */
    public EditTemplateDialog(final TemplateDTO item,
                                       final ListStore<ResourceDTO> store) {
        this();
        _mode = DialogMode.UPDATE;

        _id = item.getId();
        _store = store;
        _model = item;

        _name.setReadOnly(true); // TODO #200

        _body.setValue(_model.getBody());
        _definition.setValue(_model.getDefinition());
        _description.setValue(_model.getDescription());
        _templateTitle.setValue(_model.getTitle());
        _name.setValue(_model.getName());
    }

    private void populateFirstScreen() {
        _first.setWidth("100%");
        _first.setBorders(false);
        _first.setBodyBorder(false);
        _first.setHeaderVisible(false);

        _name.setFieldLabel(_constants.name());
        _name.setAllowBlank(false);
        _name.setId(_constants.name());
        _first.add(_name, new FormData("90%"));

        _templateTitle.setFieldLabel(_constants.title());
        _templateTitle.setAllowBlank(false);
        _templateTitle.setId(_constants.title());
        _first.add(_templateTitle, new FormData("90%"));

        _description.setFieldLabel(_constants.description());
        _description.setAllowBlank(false);
        _description.setId(_constants.description());
        _first.add(_description, new FormData("90%"));
    }

    private void populateSecondScreen() {
        _second.setWidth("100%");
        _second.setBorders(false);
        _second.setBodyBorder(false);
        _second.setHeaderVisible(false);

        _definition.setFieldLabel("content xml");
        _definition.setAllowBlank(false);
        _definition.setId("content xml");

        _second.add(_definition, new FormData("90%"));
    }

    private void populateThirdScreen() {
        _third.setWidth("100%");
        _third.setBorders(false);
        _third.setBodyBorder(false);
        _third.setHeaderVisible(false);

        _body.setFieldLabel(_constants.body());
        _body.setAllowBlank(false);
        _body.setId(_constants.body());

        _third.add(_body, new FormData("90%"));
    }

    private TemplateDTO model() { // TODO: update to handle version correctly.
        return new TemplateDTO(
            _id,
            -1,
            _name.getValue(),
            _templateTitle.getValue(),
            _description.getValue(),
            _body.getValue(),
            _definition.getValue());
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(createTemplates())
                    .check(Validations.notEmpty(_definition))
                    .check(Validations.notEmpty(_name))
                    .check(Validations.notEmpty(_templateTitle))
                    .check(Validations.notEmpty(_body))
                    .stopIfInError()
                    .check(uniqueTemplateName(_name))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Validator uniqueTemplateName(final TextField<String> name) {

        return new Validator() {
            public void validate(final Validate validate) {
                Globals.resourceService().templateNameExists(
                    name.getValue(),
                    new ErrorReportingCallback<Boolean>(){
                        public void onSuccess(final Boolean nameExists) {
                            if (nameExists) {
                                validate.addMessage(
                                    "A template with name '"
                                    + name.getValue()
                                    + "' already exists in this folder."
                                );
                            }
                            validate.next();
                        }});
            }

        };
    }

    private Runnable createTemplates() {
        return new Runnable() {
            public void run() {
                final TemplateDTO dto = model();
                if (dto.isValid()) {
                    switch (_mode) {
                        case CREATE:
                            _resourceService.createTemplate(
                            dto,
                            new DisposingCallback(EditTemplateDialog.this));
                            break;
                        case UPDATE:
                            _resourceService.updateTemplate(
                                dto,
                                new ErrorReportingCallback<Void>(){
                                    public void onSuccess(final Void arg0) {
                                        _model.set("title", dto.getTitle());
                                        // TODO name update is not persisted.
                                        _model.set("name", dto.getName());
                                        _model.set("description",
                                            dto.getDescription());
                                        _model.set("body", dto.getBody());
                                        _model.set("definition",
                                            dto.getDefinition());
                                        _store.update(_model);
                                        hide();
                                    }});
                            break;
                        default:
                            Globals.alert("Error.");
                            break;
                    }
                } else {
                    // TODO Reinstate feedback panel
                    Globals.alert(dto.validate().toString());
//                    _feedbackPanel.displayErrors(dto.validate());
//                    _feedbackPanel.setVisible(true);
                }
            }
        };
    }

}
