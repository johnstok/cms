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
package ccc.contentcreator.client;

import ccc.contentcreator.api.UIConstants;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
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
    private String _initialText;

    @SuppressWarnings("unused")
    private JavaScriptObject _editor;

    /**
     * Constructor.
     *
     * @param id The ID of the editor.
     * @param initialText The initial text of the editor.
     */
    public CodeMirrorEditor(final String id, final String initialText) {
        super();
        _id = id;
        _initialText = initialText;
        initWidget();
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
        final RadioGroup radioGroup = new RadioGroup();

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

        radioGroup.setFieldLabel(constants.syntax());
        radioGroup.add(radioHTML);
        radioGroup.add(radioJS);
        radioGroup.add(radioCSS);
        radioGroup.add(radioNone);

        radioGroup.addListener(Events.Change, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(final FieldEvent be) {
                if (radioGroup.getValue() != null) {
                    setParser(radioGroup.getValue().getValueAttribute());
                }
            }
        });
        return radioGroup;
    }

    /** {@inheritDoc} */
    @Override
    protected void onLoad() {
        _editor = initCodeMirror(_id, _initialText);
    }

    /**
     * Initialises CodeMirror editor.
     *
     * @param id The ID of the editor.
     * @param initialText The initial text of the editor.
     * @return The editor instance.
     */
    public native JavaScriptObject initCodeMirror(final String id,
                                                  final String initialText) /*-{

            var editor = $wnd.CodeMirror.fromTextArea(id, {
                height: "300px",
                parserfile: ["parsedummy.js",
                             "parsexml.js",
                             "parsecss.js",
                             "tokenizejavascript.js",
                             "parsejavascript.js",
                             "parsehtmlmixed.js"],
                stylesheet: ["/ccc/static/js/codemirror/css/xmlcolors.css",
                             "/ccc/static/js/codemirror/css/jscolors.css",
                             "/ccc/static/js/codemirror/css/csscolors.css"],
                path: "/ccc/static/js/codemirror/js/",
                continuousScanning: 1000,
                lineNumbers: true,
                textWrapping: false,
                tabMode: "spaces",
                content: initialText
              });

              return editor;
    }-*/;

    /**
     * Accessor for editor content.
     *
     * @return The content of the editor.
     */
    public native String getEditorCode()/*-{
        var ed = this.@ccc.contentcreator.client.CodeMirrorEditor::_editor;
        return ed.getCode();
    }-*/;

    /**
     * Mutator for editor content.
     *
     * @param code The content to set.
     */
    public native void setEditorCode(final String code)/*-{
        var txed = this.@ccc.contentcreator.client.CodeMirrorEditor::_editor;
        $wnd.setTimeout(function(a, b){
            txed.setCode(code);
        }, 1000);
    }-*/;

    /**
     * Set parser for the editor.
     *
     * @param parser Parser name like CSSParser.
     */
    public native void setParser(final String parser)/*-{
        var txed = this.@ccc.contentcreator.client.CodeMirrorEditor::_editor;
        $wnd.setTimeout(function(a, b){
            txed.setParser(parser);
        }, 1000);
    }-*/;

    /**
     * Set editor wrapper height.
     *
     * @param height The new height of the editor wrapping.
     */
    public native void setEditorHeight(final String height)/*-{
        var txed = this.@ccc.contentcreator.client.CodeMirrorEditor::_editor;
        if (txed) {
            txed.wrapping.style.height = height;
        }
    }-*/;

}
