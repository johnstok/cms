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

package ccc.contentcreator.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Hidden;

/**
 * Implements the <a href="http://www.fckeditor.net">FCKEditor</a> editor
 * as a Google Web Toolkit component.
 *
 * <p>The editor version is currently FCKEditor 2.4.2</p>
 *
 * <h3>Installation</h3>
 *
 * <p>This component contains the source and "compiled" code for the fckEditor
 * DHTML text  editor. To use this component, insert the following code into
 * the  HTML  &lt;head&gt;  area  of  the  page  containing  the GWT components:
 *
 * <pre>
 *     &lt;script language="javascript" type="text/javascript" src="js/htmlarea/fckeditor/fckeditor.js"&gt;&lt;/script&gt;
 * </pre>
 *
 * You should ensure that this code appears before the gwt.js script declaration.</p>
 *
 * <h3>Warning!</h3>
 *
 * <p>It also seems that the FCKEditor is not quite as nice as some of the other
 * editors about informing callers when it's fully loaded. As a result, trying
 * to set the text in the editor prematurely may not work correctly. Instead,
 * you should setup the initial text in the constructor.</p>
 *
 * <h3>Example</h3>
 *
 * <pre>
 * FCKEditor fckEditorEditArea = new FCKEditor("&lt;p&gt;Some &lt;b&gt;initial &lt;i&gt;content&lt;/i&gt;&lt;/b&gt; for the editor!&lt;/p&gt;", "", "800px", "500px");
 * panel.add(fckEditorEditArea);
 * </pre>
 *
 * @author sstorey
 */
public class FCKEditor extends Composite implements HasHTML {

    private String elementID;
    private FlowPanel panel;

    public FCKEditor(final String html, final String config, final String cssWidth, final String cssHeight) {
        //Work out an ID
        elementID = "net-sf-jwc-gwt-fckeditor-client-ui-FCKEditor"+System.identityHashCode(this);

        //Create the FlowPanel which will contain the hidden input field, and
        //iframe for the editor
        panel = new FlowPanel();

        //Create the hidden input box
        final Hidden inputBox = new Hidden();
        inputBox.setID(elementID);
        inputBox.setVisible(false);
        inputBox.setValue(html == null || html.equals("") ? " " : html);
        panel.add(inputBox);

        //Create the configuration input box
        final Hidden configBox = new Hidden();
        configBox.setID(elementID + "___Config");
        configBox.setVisible(false);
        configBox.setValue("");
        panel.add(configBox);

        //Create the IFRAME
        final Frame editorFrame = new Frame();
//        editorFrame.setUrl(getFckBaseUrl() + "editor/fckeditor.html?InstanceName=" + elementID+"&Toolbar=Basic");
        editorFrame.setUrl(getFckBaseUrl() + "editor/fckeditor.html?InstanceName=" + elementID);
        editorFrame.setSize(cssWidth, cssHeight);
        DOM.setElementProperty(editorFrame.getElement(), "scrolling", "no");
        DOM.setElementProperty(editorFrame.getElement(), "id", elementID + "___Frame");
        panel.add(editorFrame);

        initWidget(panel);
    }

    /**
     * Returns the HTML currently contained in the editor
     *
     * @return the HTML currently contained in the editor
     */
    public String getHTML() {
        return jsniGetText(elementID);
    }

    /**
     * Returns <code>null</code> as this editor doesn't support straight text editing.
     *
     * @return <code>null</code> as this editor doesn't support straight text editing.
     */
    public String getText() {
        return null;
    }

    /**
     * Sets the HTML currently contained in the editor
     *
     * @param html the HTML currently contained in the editor
     */
    public void setHTML(final String html) {
        jsniSetText(elementID, html);
    }

    /**
     * Does nothing as straight text editing is not supported by this editor
     *
     * @param text ignored
     */
    public void setText(final String text) {
        //Do nothing
    }

    // -- PRIVATE METHODS

    private String getFckBaseUrl() {
        return GWT.getModuleBaseURL()+"js/fckeditor/";
    }

    // -- NATIVE METHODS

    private static native String jsniGetText(String elementID) /*-{
        if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null) {
                return instance.GetXHTML(true);
            } else {
                //The instance isn't bound yet
                return null;
            }
        } else {
            //We're not bound yet in some way
            return null;
        }
    }-*/;

    private static native void jsniSetText(String elementID, String html) /*-{
        if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null) {
                return instance.SetHTML(html);
            } else {
                //The instance isn't bound yet
                return;
            }
        } else {
            //We're not bound yet in some way
            return;
        }
    }-*/;
}
