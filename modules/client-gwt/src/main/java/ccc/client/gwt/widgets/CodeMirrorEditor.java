/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import ccc.client.i18n.UIConstants;

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
    private final String _id;

    @SuppressWarnings("unused")
    private JavaScriptObject _editor;
    private final RadioGroup _radioGroup = new RadioGroup();
    private boolean _ready = false;
    private final EditorListener _bus;
    private final Type _type;
    private final boolean _readOnly;
    private final int _height;

    /**
     * Constructor.
     *
     * @param id The ID of the editor.
     * @param bus The event bus.
     * @param type The of the editor.
     * @param readOnly The editor read only value for the config.
     */
    public CodeMirrorEditor(final String id,
                            final EditorListener bus,
                            final Type type,
                            final boolean readOnly) {
        super();
        _id = id;
        _bus = bus;
        _type = type;
        _readOnly = readOnly;
        _height = 300;
        initWidget();
    }

    /**
     * Constructor.
     *
     * @param id The ID of the editor.
     * @param bus The event bus.
     * @param type The of the editor.
     * @param readOnly The editor read only value for the config.
     */
    public CodeMirrorEditor(final String id,
                            final EditorListener bus,
                            final Type type,
                            final boolean readOnly,
                            final int height) {
        super();
        _id = id;
        _bus = bus;
        _type = type;
        _readOnly = readOnly;
        _height = height;
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
            GWT.getModuleBaseURL()+"js/codemirror/0.94/",
            _height+"px",
            _readOnly);
    }

    /**
     * Sets ready status and send an event.
     *
     */
    public void onInitialized() {
        _ready = true;
        _radioGroup.setEnabled(true);
        _bus.onInitialized(_type, this);
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
     * @param readOnly The editor read only value for the config.
     * @return The editor instance.
     */
    private native JavaScriptObject initCodeMirror(final CodeMirrorEditor obj,
                                                   final String id,
                                                   final String baseUrl,
                                                   final String h,
                                                   final boolean readOnly) /*-{
        initCMCallback = function() {
           obj.@ccc.client.gwt.widgets.CodeMirrorEditor::onInitialized()();
        }

        var editor = $wnd.CodeMirror.fromTextArea(id, {
            height: h,
            parserfile: ["parsedummy.js",
                         "parsexmlvelocity.js",
                         "parsecss.js",
                         "tokenizejavascript.js",
                         "parsejavascript.js",
                         "parsehtmlmixed.js"],
            stylesheet: [baseUrl+"css/xmlcolors.css",
                         baseUrl+"css/jscolors.css",
                         baseUrl+"css/csscolors.css",
                         baseUrl+"css/velocitycolors.css"],
            path: baseUrl+"js/",
            continuousScanning: 1000,
            textWrapping: false,
            lineNumbers: true,
            tabMode: "spaces",
            content: " ",
            readOnly: readOnly,
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
        var ed = this.@ccc.client.gwt.widgets.CodeMirrorEditor::_editor;
        return ed.getCode();
    }-*/;

    /**
     * Mutator for editor content.
     *
     * @param code The content to set.
     */
    public native void setEditorCode(final String code)/*-{
        var txed = this.@ccc.client.gwt.widgets.CodeMirrorEditor::_editor;
        txed.setCode(code);
    }-*/;

    /**
     * Set parser for the editor.
     *
     * @param parser Parser name like CSSParser.
     */
    public native void setParser(final String parser)/*-{
        var txed = this.@ccc.client.gwt.widgets.CodeMirrorEditor::_editor;
        if (txed) {
            txed.setParser(parser);
        }
    }-*/;

    /**
     * Set editor wrapper height.
     *
     * @param height The new height of the editor wrapping.
     */
    public native void setEditorHeight(final String height)/*-{
        var txed = this.@ccc.client.gwt.widgets.CodeMirrorEditor::_editor;
        if (txed) {
            txed.wrapping.style.height = height;
        }
    }-*/;

    /**
     * Listener for editor events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface EditorListener {

        /**
         * Indicates that an editor is ready.
         *
         * @param type The type of the editor.
         * @param editor The editor object.
         */
        void onInitialized(CodeMirrorEditor.Type type, CodeMirrorEditor editor);
    }
}
