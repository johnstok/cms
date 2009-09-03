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

import java.util.UUID;

import ccc.contentcreator.actions.CreateTemplateAction;
import ccc.contentcreator.actions.TemplateNameExistsAction;
import ccc.contentcreator.actions.UpdateTemplateAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.contentcreator.validation.Validator;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.types.MimeType;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.http.client.Response;


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
    protected static final int TEXT_AREA_HEIGHT = 350;


    private final FormPanel _first = new FormPanel();
    private final FormPanel _second = new FormPanel();
    private final FormPanel _third = new FormPanel();

    private final TextField<String> _name = new TextField<String>();
    private final TextField<String> _mimePrimary = new TextField<String>();
    private final TextField<String> _mimeSub = new TextField<String>();
    private final TextArea _body = new TextArea();
    private final TextArea _definition = new TextArea();

    private UUID _id;
    private final UUID _parentFolderId;
    private DialogMode _mode;
    private final SingleSelectionModel _ssm;
    private TemplateDelta _model;
    private ResourceSummaryModelData _proxy;

    /**
     * Constructor.
     *
     * @param parentFolderId The id of the parent folder.
     * @param ssm The selection model.
     */
    public EditTemplateDialog(final UUID parentFolderId,
                              final SingleSelectionModel ssm) {
        super(new IGlobalsImpl().uiConstants().editTemplate(),
              new IGlobalsImpl());
        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);

        _parentFolderId = parentFolderId;
        _ssm = ssm;

        _name.setId("name");
        _mimePrimary.setId("mime-primary");
        _mimeSub.setId("mime-sub");

        populateFirstScreen();
        populateSecondScreen();
        populateThirdScreen();

        addCard(_first);
        addCard(_second);
        addCard(_third);

        _mode = DialogMode.CREATE;

        addListener(Events.Resize,
            new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                final int height =
                    be.getHeight()-(IGlobals.DEFAULT_HEIGHT - TEXT_AREA_HEIGHT);
                if (height > (IGlobals.DEFAULT_HEIGHT - TEXT_AREA_HEIGHT)) {
                    _definition.setHeight(height);
                    _body.setHeight(height);
                }
            }
        });

        refresh();
    }

    /**
     * Constructor.
     *
     * @param model The template to update.
     * @param proxy The resource model.
     * @param ssm The selection model.
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
        _name.setValue(proxy.getName());
        _mimePrimary.setValue(model.getMimeType().getPrimaryType());
        _mimeSub.setValue(model.getMimeType().getSubType());
    }

    private void populateFirstScreen() {
        _first.setWidth("100%");
        _first.setBorders(false);
        _first.setBodyBorder(false);
        _first.setHeaderVisible(false);

        _name.setFieldLabel(_constants.name());
        _name.setAllowBlank(false);
        _name.setId("name");
        _first.add(_name, new FormData("95%"));

        _mimePrimary.setFieldLabel(_constants.mimePrimaryType());
        _mimePrimary.setAllowBlank(false);
        _first.add(_mimePrimary, new FormData("95%"));

        _mimeSub.setFieldLabel(_constants.mimeSubType());
        _mimeSub.setAllowBlank(false);
        _first.add(_mimeSub, new FormData("95%"));
    }

    private void populateSecondScreen() {
        _second.setWidth("100%");
        _second.setBorders(false);
        _second.setBodyBorder(false);
        _second.setHeaderVisible(false);

        _definition.setFieldLabel("content xml");
        _definition.setAllowBlank(false);
        _definition.setId("content_xml");
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
        _body.setId("body");
        _body.setHeight(TEXT_AREA_HEIGHT);

        _third.add(_body, new FormData("95%"));
    }

    private TemplateDelta model() {
        final TemplateDelta delta =
            new TemplateDelta(
                _body.getValue(),
                _definition.getValue(),
                new MimeType(_mimePrimary.getValue(), _mimeSub.getValue())
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
                    .check(Validations.notEmpty(_body))
                    .check(Validations.notEmpty(_mimePrimary))
                    .check(Validations.notEmpty(_mimeSub))
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
                    new TemplateNameExistsAction(name.getValue()){

                        @Override
                        protected void execute(final boolean nameExists) {
                            if (nameExists) {
                                validate.addMessage(
        _messages.templateWithNameAlreadyExistsInThisFolder(name.getValue())
                                );
                            }
                            validate.next();
                        }
                    }.execute();
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
                        new CreateTemplateAction(
                            _parentFolderId,
                            _name.getValue(),
                            delta){
                                @Override protected void execute(final ResourceSummary template) {
                                    _ssm.create(
                                        new ResourceSummaryModelData(template),
                                        _ssm.treeSelection());
                                    hide();
                                }
                            }.execute();
                        break;
                    case UPDATE:
                        new UpdateTemplateAction(_id, delta) {
                            /** {@inheritDoc} */
                            @Override protected void onNoContent(final Response response) {
                                _ssm.update(_proxy);
                                hide();
                            }
                        }.execute();
                        break;
                    default:
                        _globals.alert(constants().error());
                    break;
                }
            }
        };
    }

}
