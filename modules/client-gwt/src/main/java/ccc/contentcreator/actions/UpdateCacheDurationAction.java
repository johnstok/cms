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

import java.util.UUID;

import ccc.contentcreator.client.GwtJson;
import ccc.serialization.JsonKeys;
import ccc.types.Duration;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for cache duration updating.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UpdateCacheDurationAction
    extends
        RemotingAction {

    private final UUID _resourceId;
    private final Duration _duration;


    /**
     * Constructor.
     * @param duration The new cache duration.
     * @param resourceId The resource to update.
     */
    public UpdateCacheDurationAction(final UUID resourceId,
                                      final Duration duration) {
        super(GLOBALS.uiConstants().editCacheDuration(), RequestBuilder.POST);
        _resourceId = resourceId;
        _duration = duration;
    }


    /** {@inheritDoc} */
    @Override protected String getPath() {
        return "/resources/"+_resourceId+"/duration";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.CACHE_DURATION, _duration);
        json.set(JsonKeys.REVISION, (String) null);
        json.set(JsonKeys.TEMPLATE_ID, (String) null);
        return json.toString();
    }
}
