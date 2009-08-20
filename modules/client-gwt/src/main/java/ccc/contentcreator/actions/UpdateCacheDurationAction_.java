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

import ccc.api.Duration;
import ccc.api.ID;
import ccc.api.JsonKeys;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for cache duration updating.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UpdateCacheDurationAction_
    extends
        RemotingAction {

    private final ID _resourceId;
    private final Duration _duration;


    /**
     * Constructor.
     * @param duration The new cache duration.
     * @param resourceId The resource to update.
     */
    public UpdateCacheDurationAction_(final ID resourceId,
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
        return json.toString();
    }
}
