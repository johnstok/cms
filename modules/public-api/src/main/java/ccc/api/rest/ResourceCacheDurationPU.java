/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.rest;

import ccc.api.Duration;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;



/**
 * A partial update, changing a resource's cache duration.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceCacheDurationPU implements Jsonable {

    private final Duration _cacheDuration;


    /**
     * Constructor.
     *
     * @param cacheDuration The duration to set (may be NULL).
     */
    public ResourceCacheDurationPU(final Duration cacheDuration) {
        _cacheDuration = cacheDuration;
    }


    /**
     * Accessor.
     *
     * @return Returns the cacheDuration.
     */
    public final Duration getCacheDuration() {
        return _cacheDuration;
    }


    /** {@inheritDoc} */
    @Override public void toJson(final Json json) {
        json.set(JsonKeys.CACHE_DURATION, _cacheDuration);
    }
}
