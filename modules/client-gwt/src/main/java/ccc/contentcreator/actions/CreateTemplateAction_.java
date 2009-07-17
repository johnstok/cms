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
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
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
    protected String getPath() { // FIXME: Escape!
        return
            "/templates?"
            + "id="+_parentFolder
            +"&n="+_resourceName
            +"&t="+_resourceName
            +"&d=";
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
        _delta.toJson(json);
        return json.toString();
    }

    protected abstract void execute(ResourceSummary template);
}
