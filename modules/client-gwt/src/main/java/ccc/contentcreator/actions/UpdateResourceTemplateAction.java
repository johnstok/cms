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

import ccc.contentcreator.client.GwtJson;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for resource's template updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceTemplateAction
    extends
        RemotingAction {

    private final ID _resourceId;
    private final ID _templateId;


    /**
     * Constructor.
     * @param templateId The template to set.
     * @param resourceId The resource to update.
     */
    public UpdateResourceTemplateAction(final ID resourceId,
                                         final ID templateId) {
        super(UI_CONSTANTS.chooseTemplate(), RequestBuilder.POST);
        _resourceId = resourceId;
        _templateId = templateId;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/resources/"+_resourceId+"/template";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final Json json = new GwtJson();
        json.set(JsonKeys.TEMPLATE_ID, _templateId);
        return json.toString();
    }
}
