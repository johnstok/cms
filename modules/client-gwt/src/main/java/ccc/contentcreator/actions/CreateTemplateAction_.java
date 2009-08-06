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
package ccc.contentcreator.actions;

import ccc.api.ID;
import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Create a template.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateTemplateAction_
    extends
        RemotingAction {

    private final ID _parentFolder;
    private final String _resourceName;
    private final TemplateDelta _delta;

    /**
     * Constructor.
     *
     * @param parentFolderId
     * @param value
     * @param delta
     */
    public CreateTemplateAction_(final ID parentFolder,
                                 final String resourceName,
                                 final TemplateDelta delta) {
        super(GLOBALS.uiConstants().createTemplate(), RequestBuilder.POST);
        _parentFolder = parentFolder;
        _resourceName = resourceName;
        _delta = delta;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/templates";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        execute(rs);
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final Json json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _parentFolder);
        json.set(JsonKeys.DELTA, _delta);
        json.set(JsonKeys.TITLE, _resourceName);
        json.set(JsonKeys.DESCRIPTION, "");
        json.set(JsonKeys.NAME, _resourceName);
        return json.toString();
    }

    protected abstract void execute(ResourceSummary template);
}
