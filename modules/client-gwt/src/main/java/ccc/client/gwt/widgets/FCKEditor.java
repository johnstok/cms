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

import java.util.Collection;

import ccc.api.core.ResourceSummary;
import ccc.client.core.I18n;
import ccc.client.gwt.remoting.GetRootsAction;
import ccc.client.gwt.views.gxt.ImageSelectionDialog;
import ccc.client.gwt.views.gxt.LinkSelectionDialog;
import ccc.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
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

    private final String _elementID;
    private final Frame editorFrame = new Frame();
    private final ToggleButton toggleButton = new ToggleButton();
    private final HiddenField<String> _inputBox = new HiddenField<String>();
    private final HiddenField<String>  _configBox = new HiddenField<String>();
    private final Html _htmlArea = new Html();

    private boolean enabled = false;
    private String _html = "";
    UIConstants UI_CONSTANTS = I18n.UI_CONSTANTS;

    /**
     * Constructor.
     *
     * @param html The HTML to be edited.
     * @param cssHeight The height of the editor in pixels.
     */
    public FCKEditor(final String html,
                     final String cssHeight) {
        _html = html;
        setLayout(new FitLayout());
        //Work out an ID
        _elementID =
            "ccc-contentcreator-client-ui-FCKEditor"
            + System.identityHashCode(FCKEditor.this);

        _htmlArea.setBorders(true);
        _htmlArea.setHtml(_html);
        add(_htmlArea);

        toggleButton.setText(UI_CONSTANTS.edit());
        add(toggleButton);

        // Add a resize handler to adjust width.
        addListener(
            Events.Resize,
            new Listener<BoxComponentEvent>() {
                public void handleEvent(final BoxComponentEvent be) {
                    fckResize(be.getWidth());
                }
            }
        );

        toggleButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {
                if (toggleButton.isPressed()) {
                    switchToEditor(cssHeight);
                } else {
                    switchToView();
                }
            }
        });

    }

    public void fckResize(int width) {
        final String frameWidth = String.valueOf(width);
        editorFrame.setWidth(frameWidth);
        DOM.setElementProperty(
            editorFrame.getElement(), "width", frameWidth);
    }

    /**
     * Returns the HTML currently contained in the editor.
     *
     * @return the HTML currently contained in the editor
     */
    public String getHTML() {
        if(enabled) {
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
        enabled = true;
        toggleButton.setText(UI_CONSTANTS.view());
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
        new GetRootsAction() { // TODO: UseGetResourceForPathAction instead.
            @Override
            protected void onSuccess(final Collection<ResourceSummary> roots) {
                ResourceSummary rs = null;
                for (final ResourceSummary rr : roots) {
                    if (rr.getName().equals("content")) {
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
        }.execute();
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

        toggleButton.setText(UI_CONSTANTS.loading());
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
        editorFrame.setUrl(
            getFckBaseUrl()
            + "editor/fckeditor.html?InstanceName="
            + _elementID +"&Toolbar=ccc");
        editorFrame.setHeight(cssHeight);
        DOM.setElementProperty(editorFrame.getElement(), "height", cssHeight);
        DOM.setElementProperty(editorFrame.getElement(), "scrolling", "no");
        DOM.setElementProperty(
            editorFrame.getElement(), "id", _elementID + "___Frame");
        DOM.setElementPropertyInt(editorFrame.getElement(), "frameBorder", 0);

        // Build the panel
        remove(toggleButton);
        add(_configBox);
        add(_inputBox);
        add(editorFrame);
        add(toggleButton);

        fckResize(getWidth());
        layout();
    }

    private void switchToView() {

        toggleButton.setText(UI_CONSTANTS.edit());
        _html = getHTML();
        _htmlArea.setHtml(_html);
        remove(_configBox);
        remove(_inputBox);
        remove(editorFrame);
        _htmlArea.show();
        _htmlArea.setHeight(100);
        enabled = false;
    }
}
