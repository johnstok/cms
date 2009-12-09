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

    /**
     * Constructor.
     *
     * @param id The ID of the editor.
     */
    public CodeMirrorEditor(final String id) {
        super();
        _id = id;
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


    /** {@inheritDoc} */
    @Override
    protected void onLoad() {
        _editor = initCodeMirror(_id);
    }



    /**
     * Initialises CodeMirror editor.
     *
     * @param id The ID of the editor.
     * @return The editor instance.
     */
    public native JavaScriptObject initCodeMirror(final String id) /*-{

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
                content: ' '
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
        txed.wrapping.style.height = height;
    }-*/;

}
