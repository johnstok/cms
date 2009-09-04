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
package ccc.rest.dto;

import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.Duration;



/**
 * A partial update, changing a resource's cache duration.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceCacheDurationPU implements Jsonable {

    private final Duration _cacheDuration;
    private final Long     _revision;
    private final UUID     _templateId;


    /**
     * Constructor.
     *
     * @param cacheDuration The duration to set (may be NULL).
     */
    public ResourceCacheDurationPU(final Duration cacheDuration) {
        _cacheDuration = cacheDuration;
        _revision = null;
        _templateId = null;
    }

    /**
     * Constructor.
     *
     * @param revision The revision used to create the working copy.
     */
    public ResourceCacheDurationPU(final Long revision) {
        _revision = revision;
        _cacheDuration = null;
        _templateId = null;
    }


    /**
     * Constructor.
     *
     * @param cacheDuration The duration to set (may be NULL).
     * @param revision The revision used to create the working copy.
     * @param templateId The template id.
     */
    public ResourceCacheDurationPU(final Duration cacheDuration,
                                   final Long revision,
                                   final UUID templateId) {
        _revision = revision;
        _cacheDuration = cacheDuration;
        _templateId = templateId;
    }

    /**
     * Constructor.
     *
     * @param templateId The template id.
     */
    public ResourceCacheDurationPU(final UUID templateId) {
        _templateId = templateId;
        _cacheDuration = null;
        _revision = null;
    }

    /**
     * Accessor.
     *
     * @return Returns the cacheDuration.
     */
    public final Duration getCacheDuration() {
        return _cacheDuration;
    }


    /**
     * Accessor.
     *
     * @return Returns the revision.
     */
    public final long getRevision() {
        return _revision;
    }


    /**
     * Accessor.
     *
     * @return Returns the templateId.
     */
    public final UUID getTemplateId() {
        return _templateId;
    }


    /** {@inheritDoc} */
    @Override public void toJson(final Json json) {
        json.set(JsonKeys.CACHE_DURATION, _cacheDuration);
        json.set(JsonKeys.REVISION, Long.valueOf(_revision));
        json.set(JsonKeys.TEMPLATE_ID, _templateId);
    }
}
