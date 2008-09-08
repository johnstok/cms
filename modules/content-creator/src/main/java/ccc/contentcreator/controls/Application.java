/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.controls;

import ccc.contentcreator.dialogs.ApplicationDialog;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface Application {

    void alert(String message);

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    Constants constants();

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    String hostURL();

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    PanelControl verticalPanel();

    /**
     * TODO: Add a description of this method.
     * @param title
     *
     * @return
     */
    ApplicationDialog dialog(String title);

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    Control frame(String url);

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    PanelControl horizontalPanel();

    /**
     * TODO: Add a description of this method.
     *
     * @param buttonTitle
     * @param clickListener
     * @return
     */
    Control button(String buttonTitle, ClickListener clickListener);

    /**
     * TODO: Add a description of this method.
     *
     * @param class1
     * @return
     */
    ResourceServiceAsync lookupService();

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @return
     */
    Control label(String string);

    /**
     * TODO: Add a description of this method.
     *
     * @param numRows
     * @param i
     * @return
     */
    GridControl grid(int numRows, int i);

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    StringControl textBox();

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    StringControl textArea();

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    ListControl listBox();

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    FileControl fileUpload();

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    StringControl hidden();

    /**
     * TODO: Add a description of this method.
     *
     * @param url
     * @param encoding
     * @param httpMethod
     * @param formHandler
     * @param _gui
     * @return
     */
    PanelControl formPanel(String url,
                           String encoding,
                           String httpMethod,
                           FormHandler formHandler,
                           PanelControl gui);
}
