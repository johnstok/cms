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

import static ccc.client.core.InternalServices.validator;

import java.util.UUID;

import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceType;
import ccc.client.actions.CreateTemplateAction;
import ccc.client.actions.UpdateTemplateAction;
import ccc.client.core.DialogMode;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.SingleSelectionModel;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.widgets.CodeMirrorEditor;
import ccc.client.gwt.widgets.CodeMirrorEditor.EditorListener;
import ccc.client.gwt.widgets.CodeMirrorEditor.Type;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.Window;


/**
 * Dialog for creating / updating templates.
 *
 * @author Civic Computing Ltd.
 */
public class EditTemplateDialog
    extends
        AbstractWizardDialog
    implements
        EditorListener {


    /** DEFAULT_WIDTH : int. */
    protected static final int DEFAULT_WIDTH = 640;
    /** DEFAULT_HEIGHT : int. */
    protected static final int DEFAULT_HEIGHT = 430;
    /** TEXT_AREA_HEIGHT : int. */
    protected static final int TEXT_AREA_HEIGHT = 300;


    private final FormPanel _first = new FormPanel();
    private final FormPanel _second = new FormPanel();
    private final PreviewFormPanel _third = new PreviewFormPanel();
    private HiddenField<String> _postBody = new HiddenField<String>();
    private final TriggerField<String> _targetPath = new TriggerField<String>();

    private final TextField<String> _name = new TextField<String>();
    private final TextField<String> _mimePrimary = new TextField<String>();
    private final TextField<String> _mimeSub = new TextField<String>();
    private CodeMirrorEditor _body;
    private CodeMirrorEditor _definition;

    private Template _model;
    private UUID _parentFolderId = null;
    private final DialogMode _mode;
    private final SingleSelectionModel _ssm;
    private ResourceSummary _proxy;
    private final String _definitionString;
    private final String _bodyString;
    

    /**
     * Constructor.
     *
     * @param parentFolderId The id of the parent folder.
     * @param ssm The selection model.
     */
    public EditTemplateDialog(final UUID parentFolderId,
                              final SingleSelectionModel ssm) {
        super(I18n.uiConstants.editTemplate(),
            InternalServices.globals);
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
    public EditTemplateDialog(final Template model,
                              final ResourceSummary proxy,
                              final SingleSelectionModel ssm) {
        super(I18n.uiConstants.editTemplate(), InternalServices.globals);
        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);
        _mode = DialogMode.UPDATE;

        _proxy = proxy;
        _ssm = ssm;

        _model = model;
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

        _name.setValue(proxy.getName().toString());
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
            CodeMirrorEditor.Type.DEFINITION,
            false);
        _second.add(_definition, new FormData("95%"));
    }

    private void populateThirdScreen() {
    	_third.setMethod(Method.POST);
    	_third.setTarget("_templatePreview");
    	_third.setWidth("100%");
    	_third.setBorders(false);
    	_third.setBodyBorder(false);
    	_third.setHeaderVisible(false);

    	_postBody.setName("hiddenbody");
        _third.add(_postBody);
        
        _targetPath.setFieldLabel(constants().path());
        _targetPath.setValue("");
        _targetPath.addListener(Events.TriggerClick, new TargetListener());

        Button previewButton = 
            new Button("preview", new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    Window.open("", "_templatePreview","");
                    _third.setAction(InternalServices.globals.appURL()
                        +"previewtemplate"+_targetPath.getValue());
                    _postBody.setValue(_body.getEditorCode());
                    _third.submit();
                }
            });

        HorizontalPanel previewPanel = new HorizontalPanel();
        FormLayout layout = new FormLayout();
        LayoutContainer lc = new LayoutContainer(layout);
        lc.add(_targetPath);
        
        previewPanel.setWidth("95%");
        previewPanel.setTableWidth("100%");
        TableData tdr = new TableData();
        TableData tdl = new TableData();
        tdr.setHorizontalAlign(HorizontalAlignment.RIGHT);
        tdl.setHorizontalAlign(HorizontalAlignment.LEFT);

        previewPanel.add(lc, tdl);
        previewPanel.add(previewButton, tdr);
        _third.add(previewPanel);

        final Text fieldName = new Text(getUiConstants().body());
        fieldName.setStyleName("x-form-item");
        _third.add(fieldName);
        _body = new CodeMirrorEditor(
            "body",
            this,
            CodeMirrorEditor.Type.BODY,
            false);
        _third.add(_body, new FormData("95%"));
    }

    private Template model() {
        final Template delta = new Template();
        delta.setBody(_body.getEditorCode());
        delta.setDefinition(_definition.getEditorCode());
        delta.setMimeType(
            new MimeType(_mimePrimary.getValue(), _mimeSub.getValue()));
        return delta;
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {

                final ValidationResult vr = new ValidationResult();
                vr.addError(
                    validator.notEmpty(
                        _definition.getEditorCode(),
                        getUiConstants().definitionXML()));
                vr.addError(
                    validator.notEmpty(
                        _name.getValue(), _name.getFieldLabel()));
                vr.addError(
                    validator.notEmpty(
                        _body.getEditorCode(), getUiConstants().body()));
                vr.addError(
                    validator.notEmpty(
                        _mimePrimary.getValue(), _mimePrimary.getFieldLabel()));
                vr.addError(
                    validator.notEmpty(
                        _mimeSub.getValue(), _mimeSub.getFieldLabel()));
                vr.addError(
                    validator.notValidResourceName(
                        _name.getValue(), _name.getFieldLabel()));
                vr.addError(
                    validator.notValidXML(_definition.getEditorCode()));

                if (!vr.isValid()) {
                    InternalServices.window.alert(vr.getErrorText());
                    return;
                }

                createTemplates();
            }
        };
    }


    private void createTemplates() {
        final Template delta = model();
        switch (_mode) {
            case CREATE:

                delta.setTitle(_name.getValue());
                delta.setDescription(_name.getValue());
                delta.setName(new ResourceName(_name.getValue()));
                delta.setParent(_parentFolderId);

                new CreateTemplateAction(delta){
                        @Override protected void execute(
                                     final Template template) {
                            _ssm.create(template);
                            hide();
                        }
                    }.execute();
                break;
            case UPDATE:

                delta.setId(_proxy.getId());
                delta.addLink(
                    Resource.Links.SELF, _model.getLink(Resource.Links.SELF));

                new UpdateTemplateAction(delta) {
                    /** {@inheritDoc} */
                    @Override protected void onSuccess(final Template t) {
                        _ssm.update(_proxy);
                        hide();
                    }
                }.execute();
                break;
            default:
                InternalServices.window.alert(constants().error());
            break;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onInitialized(final Type type, final CodeMirrorEditor editor) {
        // FIXME: Dodgy.
        if (CodeMirrorEditor.Type.BODY == type) {
            editor.setEditorCode(_bodyString);
        } else if (CodeMirrorEditor.Type.DEFINITION == type) {
            editor.setEditorCode(_definitionString);
        }
    }

    
    /**
     * Class required to override private 'setTarget' method in FormPanel. 
     * 
     * @author petteri
     *
     */
    public class PreviewFormPanel extends FormPanel {
    	public native void setTarget(String target)/*-{
		this.@com.extjs.gxt.ui.client.widget.form.FormPanel::setTarget(Ljava/lang/String;)(target);
	}-*/;
    };
    
    
    public class TargetListener implements Listener<ComponentEvent> {
        public void handleEvent(final ComponentEvent be) {
        	
            ResourceSummary root = null;

            for (final ResourceSummary item : InternalServices.roots.getElements()) {
                if (item.getName().toString().equals("content")) {
                    root = item;
                }
            }
        	
            final ResourceSelectionDialog resourceSelect =
            	new ResourceSelectionDialog(root, null);
            resourceSelect.addListener(Events.Hide,
            		new Listener<ComponentEvent>() {
            	public void handleEvent(final ComponentEvent be2) {
            		final ResourceSummary target =
            			resourceSelect.selectedResource();
            		if (target != null
            				&& target.getType() != ResourceType.RANGE_FOLDER) {
            			_targetPath.setValue(target.getAbsolutePath());
            		}
            	}});
            resourceSelect.show();
        }
    }
}
