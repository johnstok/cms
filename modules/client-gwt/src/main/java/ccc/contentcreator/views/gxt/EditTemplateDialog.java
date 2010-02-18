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

package ccc.contentcreator.views.gxt;

import java.util.UUID;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.DialogMode;
import ccc.contentcreator.core.Event;
import ccc.contentcreator.core.EventBus;
import ccc.contentcreator.core.IGlobalsImpl;
import ccc.contentcreator.core.SingleSelectionModel;
import ccc.contentcreator.core.Event.Type;
import ccc.contentcreator.events.CMEditorReadyEvent;
import ccc.contentcreator.remoting.CreateTemplateAction;
import ccc.contentcreator.remoting.TemplateNameExistsAction;
import ccc.contentcreator.remoting.UpdateTemplateAction;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.contentcreator.validation.Validator;
import ccc.contentcreator.widgets.CodeMirrorEditor;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.types.MimeType;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.http.client.Response;


/**
 * Dialog for creating / updating templates.
 *
 * @author Civic Computing Ltd.
 */
public class EditTemplateDialog extends AbstractWizardDialog
    implements EventBus {

    /** DEFAULT_WIDTH : int. */
    protected static final int DEFAULT_WIDTH = 640;
    /** DEFAULT_HEIGHT : int. */
    protected static final int DEFAULT_HEIGHT = 430;
    /** TEXT_AREA_HEIGHT : int. */
    protected static final int TEXT_AREA_HEIGHT = 300;


    private final FormPanel _first = new FormPanel();
    private final FormPanel _second = new FormPanel();
    private final FormPanel _third = new FormPanel();

    private final TextField<String> _name = new TextField<String>();
    private final TextField<String> _mimePrimary = new TextField<String>();
    private final TextField<String> _mimeSub = new TextField<String>();
    private CodeMirrorEditor _body;
    private CodeMirrorEditor _definition;

    private UUID _id;
    private UUID _parentFolderId = null;
    private DialogMode _mode;
    private final SingleSelectionModel _ssm;
    private ResourceSummaryModelData _proxy;
    private String _definitionString;
    private String _bodyString;

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
        _mode = DialogMode.CREATE;

        _parentFolderId = parentFolderId;
        _ssm = ssm;

        _definitionString = "<fields></fields>";
        _bodyString = "<html/>";
        populateFirstScreen();
        populateSecondScreen();
        populateThirdScreen();

        addCard(_first);
        addCard(_second);
        addCard(_third);

        addResizeListener();

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
        super(new IGlobalsImpl().uiConstants().editTemplate(),
            new IGlobalsImpl());
        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);
        _mode = DialogMode.UPDATE;

        _proxy = proxy;
        _id = proxy.getId();
        _ssm = ssm;

        _definitionString = model.getDefinition();
        _bodyString = model.getBody();
        populateFirstScreen();
        populateSecondScreen();
        populateThirdScreen();

        addCard(_first);
        addCard(_second);
        addCard(_third);

        addResizeListener();

        _name.setReadOnly(true);
        _name.disable();

        _name.setValue(proxy.getName());
        _mimePrimary.setValue(model.getMimeType().getPrimaryType());
        _mimeSub.setValue(model.getMimeType().getSubType());
        refresh();
    }

    private void addResizeListener() {

        addListener(Events.Resize,
            new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                final int h =
                    be.getHeight()-(DEFAULT_HEIGHT - TEXT_AREA_HEIGHT);
                if (h > (DEFAULT_HEIGHT - TEXT_AREA_HEIGHT)) {
                    _definition.setEditorHeight(h+"px");
                    _body.setEditorHeight(h+"px");
                }
            }
        });
    }

    private void populateFirstScreen() {
        _first.setWidth("100%");
        _first.setBorders(false);
        _first.setBodyBorder(false);
        _first.setHeaderVisible(false);

        _name.setFieldLabel(getUiConstants().name());
        _name.setAllowBlank(false);
        _name.setId("name");
        _first.add(_name, new FormData("95%"));

        _mimePrimary.setFieldLabel(getUiConstants().mimePrimaryType());
        _mimePrimary.setAllowBlank(false);
        _first.add(_mimePrimary, new FormData("95%"));

        _mimeSub.setFieldLabel(getUiConstants().mimeSubType());
        _mimeSub.setAllowBlank(false);
        _first.add(_mimeSub, new FormData("95%"));
    }

    private void populateSecondScreen() {
        _second.setWidth("100%");
        _second.setBorders(false);
        _second.setBodyBorder(false);
        _second.setHeaderVisible(false);

        final Text fieldName = new Text(getUiConstants().definitionXML());
        fieldName.setStyleName("x-form-item");
        _second.add(fieldName);
        _definition = new CodeMirrorEditor(
            "definition",
            this,
            CodeMirrorEditor.Type.DEFINITION);
        _second.add(_definition, new FormData("95%"));
    }

    private void populateThirdScreen() {
        _third.setWidth("100%");
        _third.setBorders(false);
        _third.setBodyBorder(false);
        _third.setHeaderVisible(false);

        final Text fieldName = new Text(getUiConstants().body());
        fieldName.setStyleName("x-form-item");
        _third.add(fieldName);
        _body = new CodeMirrorEditor(
            "body",
            this,
            CodeMirrorEditor.Type.BODY);
        _third.add(_body, new FormData("95%"));
    }

    private TemplateDelta model() {
        final TemplateDelta delta =
            new TemplateDelta(
                _body.getEditorCode(),
                _definition.getEditorCode(),
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
                    .check(Validations.notEmpty(
                        _definition.getEditorCode(), "Definition"))
                    .check(Validations.notEmpty(_name))
                    .check(Validations.notValidResourceName(_name))
                    .check(Validations.notEmpty(_body.getEditorCode(), "body"))
                    .check(Validations.notEmpty(_mimePrimary))
                    .check(Validations.notEmpty(_mimeSub))
                    .stopIfInError()
                    .check(Validations.notValidXML(_definition.getEditorCode()))
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
        getMessages().templateWithNameAlreadyExistsInThisFolder(name.getValue())
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
                                @Override protected void execute(
                                             final ResourceSummary template) {
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
                            @Override protected void onNoContent(
                                                     final Response response) {
                                _ssm.update(_proxy);
                                hide();
                            }
                        }.execute();
                        break;
                    default:
                        getGlobals().alert(constants().error());
                    break;
                }
            }
        };
    }
    /** {@inheritDoc} */
    @Override
    public void put(final Event event) {
        if (Type.CM_EDITOR_READY==event.getType()) {
            final CodeMirrorEditor cme =
                ((CMEditorReadyEvent) event).getCodeMirrorEditor();
            if (cme.getType() == CodeMirrorEditor.Type.BODY) {
                cme.setEditorCode(_bodyString);
            } else if (cme.getType() == CodeMirrorEditor.Type.DEFINITION) {
                cme.setEditorCode(_definitionString);
            }
        }
    }

}
