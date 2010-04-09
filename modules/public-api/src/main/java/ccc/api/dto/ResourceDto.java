/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.dto;

import java.io.Serializable;
import java.util.UUID;

import ccc.api.types.Duration;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;



/**
 * A partial update, changing a resource's cache duration.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceDto implements Jsonable, Serializable {

    private final Duration _cacheDuration;
    private final Long     _revision;
    private final UUID     _templateId;


    /**
     * Constructor.
     *
     * @param cacheDuration The duration to set (may be NULL).
     */
    public ResourceDto(final Duration cacheDuration) {
        _cacheDuration = cacheDuration;
        _revision = null;
        _templateId = null;
    }

    /**
     * Constructor.
     *
     * @param revision The revision used to create the working copy.
     */
    public ResourceDto(final Long revision) {
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
    public ResourceDto(final Duration cacheDuration,
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
    public ResourceDto(final UUID templateId) {
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
    public final Long getRevision() {
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
        if (_revision == null) {
            json.set(JsonKeys.REVISION, (String) null);
        } else {
            json.set(JsonKeys.REVISION, _revision);
        }
        json.set(JsonKeys.TEMPLATE_ID, _templateId);
    }
}
