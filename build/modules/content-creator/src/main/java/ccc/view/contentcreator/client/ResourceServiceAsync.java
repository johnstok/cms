/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */
package ccc.view.contentcreator.client;

import java.util.Map;

import ccc.view.contentcreator.dto.TemplateDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous service for managing resources.
 *
 * @author Civic Computing Ltd
 */
public interface ResourceServiceAsync {
    void getContentRoot(AsyncCallback<String> callback);

    /**
     * TODO: Add a description of this method.
     *
     * @param absolutePath
     * @param callback
     */
    void getResource(String absolutePath, AsyncCallback<String> callback);

    void saveContent(String id, String title, Map<String, String> paragraphs, AsyncCallback<Void> callback);

    void createTemplate(final TemplateDTO dto, AsyncCallback<Void> callback);
}
