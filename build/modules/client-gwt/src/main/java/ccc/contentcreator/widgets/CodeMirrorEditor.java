/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.widgets;

import ccc.contentcreator.core.EventBus;
import ccc.contentcreator.events.CMEditorReadyEvent;
import ccc.contentcreator.i18n.UIConstants;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Wrapper class for CodeMirror.
 *
 * @author Civic Computing Ltd.
 */
public class CodeMirrorEditor extends Composite {
    private String _id;

    @SuppressWarnings("unused")
    private JavaScriptObject _editor;
    private final RadioGroup _radioGroup = new RadioGroup();
    private boolean _ready = false;
    private EventBus _bus;
    private Type _type;

    /**
     * Constructor.
     *
     * @param id The ID of the editor.
     * @param bus The event bus.
     * @param type The of the editor.
     */
    public CodeMirrorEditor(final String id,
                            final EventBus bus,
                            final Type type) {
        super();
        _id = id;
        _bus = bus;
        _type = type;
        initWidget();
    }

    /**
     * Type of the editor.
     *
     * @author Civic Computing Ltd.
     */
    public enum Type {
        /** DEFINITION : type. */
        DEFINITION,
        /** BODY : type. */
        BODY,
        /** TEXT : type. */
        TEXT
    };

    /**
     * Accessor for editor type.
     *
     * @return The type.
     */
    public Type getType(){
        return _type;
    }

    private void initWidget() {
        final VerticalPanel panel = new VerticalPanel();
        panel.setWidth("100%");
        panel.setStyleName("codemirror-ed");

        final TextArea textArea = new TextArea();
        DOM.setElementAttribute(textArea.getElement(), "id", _id);
        textArea.setVisible(false);

        panel.add(textArea);
        initWidget(panel);
    }


    /**
     * Creates populated RadioGroup for parser selection.
     *
     * @param constants UIConstants for labels.
     * @return RadioGroup of possible parsers options.
     */
    public RadioGroup parserSelector(final UIConstants constants) {

        _radioGroup.setEnabled(false);

        final Radio radioHTML = new Radio();
        radioHTML.setBoxLabel("HTML");
        radioHTML.setValueAttribute("HTMLMixedParser");
        radioHTML.setValue(true);

        final Radio radioJS = new Radio();
        radioJS.setBoxLabel("JS");
        radioJS.setValueAttribute("JSParser");
        radioJS.setValue(false);

        final Radio radioCSS = new Radio();
        radioCSS.setBoxLabel("CSS");
        radioCSS.setValueAttribute("CSSParser");
        radioCSS.setValue(false);

        final Radio radioNone = new Radio();
        radioNone.setBoxLabel(constants.text());
        radioNone.setValueAttribute("DummyParser");
        radioNone.setValue(false);

        _radioGroup.setFieldLabel(constants.syntax());
        _radioGroup.add(radioHTML);
        _radioGroup.add(radioJS);
        _radioGroup.add(radioCSS);
        _radioGroup.add(radioNone);

        _radioGroup.addListener(Events.Change, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(final FieldEvent be) {
                if (_radioGroup.getValue() != null) {
                    if (_ready) {
                        setParser(_radioGroup.getValue().getValueAttribute());
                    }
                }
            }
        });
        return _radioGroup;
    }

    /** {@inheritDoc} */
    @Override
    protected void onLoad() {
        _editor = initCodeMirror(this,
            _id,
            GWT.getModuleBaseURL()+"js/codemirror/");
    }

    /**
     * Sets ready status and send an event.
     *
     */
    public void onInitialized() {
        _ready = true;
        _radioGroup.setEnabled(true);
        _bus.put(new CMEditorReadyEvent(this));
    }

    /**
     * Returns true if editor is ready.
     *
     * @return Editor status.
     */
    public boolean isReady() {
        return _ready;
    }

    /**
     * Initialises CodeMirror editor.
     *
     * @param obj The CodeMirrorEditor instance
     * @param id The ID of the editor.
     * @param baseUrl The base URL for scripts and css.
     * @return The editor instance.
     */
    public native JavaScriptObject initCodeMirror(final CodeMirrorEditor obj,
                                                  final String id,
                                                  final String baseUrl) /*-{
        initCMCallback = function() {
           obj.@ccc.contentcreator.widgets.CodeMirrorEditor::onInitialized()();
        }

        var editor = $wnd.CodeMirror.fromTextArea(id, {
            height: "300px",
            parserfile: ["parsedummy.js",
                         "parsexml.js",
                         "parsecss.js",
                         "tokenizejavascript.js",
                         "parsejavascript.js",
                         "parsehtmlmixed.js"],
            stylesheet: [baseUrl+"css/xmlcolors.css",
                         baseUrl+"css/jscolors.css",
                         baseUrl+"css/csscolors.css"],
            path: baseUrl+"js/",
            continuousScanning: 1000,
            textWrapping: false,
            lineNumbers: true,
            tabMode: "spaces",
            content: " ",
            initCallback: initCMCallback
          });

          return editor;
    }-*/;

    /**
     * Accessor for editor content.
     *
     * @return The content of the editor.
     */
    public native String getEditorCode()/*-{
        var ed = this.@ccc.contentcreator.widgets.CodeMirrorEditor::_editor;
        return ed.getCode();
    }-*/;

    /**
     * Mutator for editor content.
     *
     * @param code The content to set.
     */
    public native void setEditorCode(final String code)/*-{
        var txed = this.@ccc.contentcreator.widgets.CodeMirrorEditor::_editor;
        txed.setCode(code);
    }-*/;

    /**
     * Set parser for the editor.
     *
     * @param parser Parser name like CSSParser.
     */
    public native void setParser(final String parser)/*-{
        var txed = this.@ccc.contentcreator.widgets.CodeMirrorEditor::_editor;
        txed.setParser(parser);
    }-*/;

    /**
     * Set editor wrapper height.
     *
     * @param height The new height of the editor wrapping.
     */
    public native void setEditorHeight(final String height)/*-{
        var txed = this.@ccc.contentcreator.widgets.CodeMirrorEditor::_editor;
        if (txed) {
            txed.wrapping.style.height = height;
        }
    }-*/;

}
