/*
 * Copyright 2007 - Steve Storey
 *
 * Licensed under the LGPL v2; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License in the root directory
 * of this project
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ccc.client.gwt.widgets;

import ccc.api.core.ResourceSummary;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.gwt.views.gxt.ImageSelectionDialog;
import ccc.client.gwt.views.gxt.LinkSelectionDialog;
import ccc.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Frame;

/**
 * Implements the <a href="http://www.fckeditor.net">FCKEditor</a> editor
 * as a GXT component.
 *
 * <p>The editor version is currently FCKEditor 2.6.5</p>
 *
 * <h3>Installation</h3>
 * <p>This component contains the source and "compiled" code for the fckEditor
 * DHTML text  editor. To use this component, insert the following code into
 * the  HTML  &lt;head&gt;  area  of  the  page  containing  the GWT components:
 * <pre>
 * &lt;script
 *     language="javascript"
 *     type="text/javascript"
 *     src="js/htmlarea/fckeditor/fckeditor.js" /&gt;
 * </pre>
 * You should ensure that this code appears before the gwt.js script
 * declaration.</p>
 *
 * <h3>Warning!</h3>
 * <p>It also seems that the FCKEditor is not quite as nice as some of the other
 * editors about informing callers when it's fully loaded. As a result, trying
 * to set the text in the editor prematurely may not work correctly. Instead,
 * you should setup the initial text in the constructor.</p>
 *
 * <h3>Example</h3>
 * <pre>
 * FCKEditor fckEditorEditArea =
 *     new FCKEditor("&lt;p&gt;Content!&lt;/p&gt;", "500px");
 * add(fckEditorEditArea);
 * </pre>
 */
public class FCKEditor extends LayoutContainer {

    private static final int HTML_HEIGHT            = 30;
    private final UIConstants         _uiConstants  = I18n.uiConstants;
    private final Frame               _editorFrame  = new Frame();
    private final ToggleButton        _toggleButton = new ToggleButton();
    private final HiddenField<String> _inputBox     = new HiddenField<String>();
    private final HiddenField<String> _configBox    = new HiddenField<String>();
    private final Html                _htmlArea     = new Html();
    private final String              _elementID;
    private final String              _tooltip;
    private final String              _label;

    private boolean _enabled = false;
    private String _html = "";

    /**
     * Constructor.
     *
     * @param html      The HTML to be edited.
     * @param cssHeight The height of the editor in pixels.
     * @param tooltip   The editor's tool-tip.
     * @param label     The editor's label.
     */
    public FCKEditor(final String html,
                     final String cssHeight,
                     final String tooltip,
                     final String label) {
        _label = label;

        if (null!=_label) {
            final Text fieldName = new Text(_label+":");
            fieldName.setTagName("label");
            fieldName.setStyleName("x-form-item-label");
            add(fieldName);
        }

        _html = html;
        _tooltip = tooltip;
        setLayout(new FitLayout());
        //Work out an ID
        _elementID =
            "ccc-contentcreator-client-ui-FCKEditor"
            + System.identityHashCode(FCKEditor.this);

        _htmlArea.setBorders(true);
        _htmlArea.setHtml(_html);
        _htmlArea.setToolTip(_tooltip);
        if (_html.isEmpty()) {
            _htmlArea.setHeight(HTML_HEIGHT);
        } else {
            _htmlArea.setAutoHeight(true);
        }
        add(_htmlArea);

        _toggleButton.setText(_uiConstants.edit());
        add(_toggleButton);

        // Add a resize handler to adjust width.
        addListener(
            Events.Resize,
            new Listener<BoxComponentEvent>() {
                public void handleEvent(final BoxComponentEvent be) {
                    fckResize(be.getWidth());
                }
            }
        );

        _toggleButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(final ButtonEvent be) {
                if (_toggleButton.isPressed()) {
                    switchToEditor(cssHeight);
                } else {
                    switchToView();
                }
            }
        });

    }


    private void fckResize(final int newWidth) {
        final String frameWidth = String.valueOf(newWidth);
        _editorFrame.setWidth(frameWidth);
        DOM.setElementProperty(
            _editorFrame.getElement(), "width", frameWidth);
    }


    /**
     * Returns the HTML currently contained in the editor.
     *
     * @return the HTML currently contained in the editor
     */
    public String getHTML() {
        if(_enabled) {
            return jsniGetText(_elementID);
        }
        return _html;
    }


    private String getFckBaseUrl() {
        return GWT.getModuleBaseURL()+"js/fckeditor/";
    }


    private static native String jsniGetText(final String elementID) /*-{
        if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null) {
                return instance.GetXHTML(true);
            }
        }
        //We're not bound yet in some way
        return null;
    }-*/;


    private static native String initJSNI(final FCKEditor obj) /*-{
        $wnd.cccLinkSelector = function(fckname, url, title, innerText, cccId, openInNew) {
            obj.@ccc.client.gwt.widgets.FCKEditor::openLinkSelector(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)(fckname,url,title,innerText,cccId,openInNew);
        };

        $wnd.cccImageSelector = function(fckname, url, alt, title, cccId) {
            obj.@ccc.client.gwt.widgets.FCKEditor::openImageSelector(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(fckname,url,alt,title,cccId);
        };

        $wnd.FCKeditor_OnComplete = function(editorInstance) {
            obj.@ccc.client.gwt.widgets.FCKEditor::checkFCK()();
        }
    }-*/;


    /**
     * Enable save buttons when FCKEditors are ready.
     */
    public void checkFCK() {
        _enabled = true;
        _toggleButton.setText(_uiConstants.view());
    }


    /**
     * Displays the FCKEditor specific link selection dialog.
     *
     * @param elementID The element id for the FCK editor instance.
     * @param url The link's URL.
     * @param title The title of the link.
     * @param innerText The inner text/HTML of the link.
     * @param cccId The ccc id stored in class of the link.
     * @param openInNew Boolean  for opening the link in the new  of the window.
     */
    public void openLinkSelector(final String elementID,
                                 final String url,
                                 final String title,
                                 final String innerText,
                                 final String cccId,
                                 final boolean openInNew) {

        ResourceSummary rs = null;
        for (final ResourceSummary rr : InternalServices.roots.getElements()) {
            if (rr.getName().toString().equals("content")) {
                rs = rr;
            }
        }
        new LinkSelectionDialog(rs,
            elementID,
            url,
            title,
            innerText,
            cccId,
            openInNew).show();
    }


    /**
     * Displays the FCKEditor specific image selection dialog.
     *
     * @param elementID The name of the FCKEditor.
     * @param url The URL of the selected image.
     * @param alt The alternative text for the selected image.
     * @param title The title of the selected image.
     * @param cccId The ccc id stored in class of the image.
     */
    public void openImageSelector(final String elementID,
                                  final String url,
                                  final String alt,
                                  final String title,
                                  final String cccId) {
        new ImageSelectionDialog(elementID, url, alt, title, cccId).show();
    }


    private void switchToEditor(final String cssHeight) {

        _toggleButton.setText(_uiConstants.loading());
        _htmlArea.hide();
        initJSNI(FCKEditor.this);

        //Create the hidden input box
        _inputBox.setId(_elementID);
        _inputBox.setVisible(false);
        _inputBox.setValue(_html == null || _html.equals("") ? " " : _html);

        //Create the configuration input box
        _configBox.setId(_elementID + "___Config");
        _configBox.setVisible(false);
        _configBox.setValue("");

        //Create the IFRAME
        _editorFrame.setUrl(
            getFckBaseUrl()
            + "editor/fckeditor.html?InstanceName="
            + _elementID +"&Toolbar=ccc");
        _editorFrame.setHeight(cssHeight);
        DOM.setElementProperty(_editorFrame.getElement(), "height", cssHeight);
        DOM.setElementProperty(_editorFrame.getElement(), "scrolling", "no");
        DOM.setElementProperty(
            _editorFrame.getElement(), "id", _elementID + "___Frame");
        DOM.setElementPropertyInt(_editorFrame.getElement(), "frameBorder", 0);

        // Build the panel
        remove(_toggleButton);
        add(_configBox);
        add(_inputBox);
        add(_editorFrame);
        add(_toggleButton);

        fckResize(getWidth());
        layout();
    }


    private void switchToView() {
        _toggleButton.setText(_uiConstants.edit());
        _html = getHTML();
        _htmlArea.setHtml(_html);
        if (_html.isEmpty()) {
            _htmlArea.setHeight(HTML_HEIGHT);
        } else {
            _htmlArea.setAutoHeight(true);
        }
        remove(_configBox);
        remove(_inputBox);
        remove(_editorFrame);
        _htmlArea.show();
        _enabled = false;
    }


    /**
     * Get the tool-tip for this editor.
     *
     * @return The tool-tip, as a string.
     */
    public String getToolTip2() { return _tooltip; }


    /**
     * Get the length of the HTML content.
     *
     * @return The content length, as an integer.
     */
    public int getLength() { return getHTML().length(); }


    /**
     * Get the editor's label.
     *
     * @return The label as a string.
     */
    public String getLabel() { return _label; }
}
