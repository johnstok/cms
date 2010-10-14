/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.DBC;
import ccc.api.types.Duration;
import ccc.api.types.Link;


/**
 * A read-only snapshot of a resource.
 *
 * @author Civic Computing Ltd.
 */
public class Resource
    extends
        ResourceSummary {

    private Duration            _cacheDuration;
    private Map<String, String> _metadata = new HashMap<String, String>();
    private int                 _revision;
    private UUID                _template; // Computed template.

    // Move to ResourceSummary
    private boolean             _isSecure;
    private UUID                _lockedBy;
    private UUID                _publishedBy;


    /**
     * Constructor.
     */
    public Resource() { super(); }


    /**
     * Constructor.
     *
     * @param cacheDuration The duration to set (may be NULL).
     */
    @Deprecated
    public Resource(final Duration cacheDuration) {
        _cacheDuration = cacheDuration;
        _revision = -1;
        _template = null;
    }

    /**
     * Constructor.
     *
     * @param revision The revision used to create the working copy.
     */
    @Deprecated
    public Resource(final Long revision) {
        _revision = revision.intValue();
        _cacheDuration = null;
        _template = null;
    }


    /**
     * Constructor.
     *
     * @param cacheDuration The duration to set (may be NULL).
     * @param revision The revision used to create the working copy.
     * @param templateId The template id.
     */
    @Deprecated
    public Resource(final Duration cacheDuration,
                    final Long revision,
                    final UUID templateId) {
        _revision = revision.intValue();
        _cacheDuration = cacheDuration;
        _template = templateId;
    }

    /**
     * Constructor.
     *
     * @param templateId The template id.
     */
    @Deprecated
    public Resource(final UUID templateId) {
        _template = templateId;
        _cacheDuration = null;
        _revision = -1;
    }


    /**
     * Compute the template for this resource.
     *
     * @return The selected template.
     */
    public final UUID getTemplate() {
        return _template;
    }


    /**
     * Compute the cache duration for this resource.
     *
     * @return The computed duration.
     */
    public final Duration getCacheDuration() {
        return _cacheDuration;
    }


    /**
     * Retrieve metadata for this resource.
     *
     * @param key The key with which the datum was stored.
     * @return The value of the datum. NULL if the datum doesn't exist.
     */
    public final String getMetadatum(final String key) {
        return _metadata.get(key);
    }


    /**
     * Accessor.
     *
     * @return True if the resource is locked, false otherwise.
     */
    public final boolean isLocked() {
        return null != _lockedBy;
    }


    /**
     * Accessor.
     *
     * @return True if the resource is published, false otherwise.
     */
    public final boolean isPublished() {
        return _publishedBy != null;
    }


    /**
     * Accessor.
     *
     * @return The user that locked the resource or false if the resource is not
     *  locked.
     */
    public final UUID getLockedById() {
        return _lockedBy;
    }


    /**
     * Accessor.
     *
     * @return The resource's metadata, as a map.
     */
    public final Map<String, String> getMetadata() {
        return _metadata;
    }


    /**
     * Accessor.
     *
     * @return The user that published the resource or null if the resource
     *  isn't published.
     */
    public final UUID getPublishedById() {
        return _publishedBy;
    }


    /**
     * Accessor.
     *
     * @return True if the resource requires security privileges to access;
     *  false otherwise.
     */
    public boolean isSecure() {
        return _isSecure;
    }


    /**
     * Mutator.
     *
     * @param isSecure The isSecure to set.
     */
    public void setSecure(final boolean isSecure) {
        _isSecure = isSecure;
    }


    /**
     * Mutator.
     *
     * @param tags The tags to set.
     */
    @Override
    public void setTags(final String tags) {
        setTags(parseTagString(tags));
    }


    private Set<String> parseTagString(final String tags) {
        DBC.require().notNull(tags);
        DBC.require().containsNoBrackets(tags);

        final String[] tagArray = tags.split(",");
        final Set<String> parsed = new HashSet<String>();
        for(final String tag : tagArray) {
            if (tag.trim().length() < 1) {
                continue;
            }
            parsed.add(tag.trim());
        }
        return parsed;
    }


    /**
     * Mutator.
     *
     * @param publishedBy The publishedBy to set.
     */
    public void setPublishedBy(final UUID publishedBy) {
        _publishedBy = publishedBy;
    }


    /**
     * Mutator.
     *
     * @param metadata The metadata to set.
     */
    public void setMetadata(final Map<String, String> metadata) {
        _metadata =
            (null==metadata)
                ? new HashMap<String, String>()
                : new HashMap<String, String>(metadata);
    }


    /**
     * Mutator.
     *
     * @param lockedBy The lockedBy to set.
     */
    public void setLockedBy(final UUID lockedBy) {
        _lockedBy = lockedBy;
    }


    /**
     * Mutator.
     *
     * @param template The template to set.
     */
    public void setTemplate(final UUID template) {
        _template = template;
    }



    /**
     * Mutator.
     *
     * @param cacheDuration The cacheDuration to set.
     */
    public void setCacheDuration(final Duration cacheDuration) {
        _cacheDuration = cacheDuration;
    }


    /**
     * Mutator.
     *
     * @param revision The revision to set.
     */
    public void setRevision(final int revision) {
        _revision = revision;
    }


    /**
     * Accessor.
     *
     * @return Returns the revision.
     */
    public int getRevision() {
        return _revision;
    }


    /**
     * Accessor.
     *
     * @return True if the snapshot is for a working copy, false otherwise.
     */
    public boolean isWorkingCopy() {
        return _revision<0;
    }


    /**
     * Query.
     *
     * @return True if the resource is cache-able, false otherwise.
     */
    public boolean isCacheable() {
        return null!=_cacheDuration && _cacheDuration.time()>0;
    }


    /**
     * Link.
     *
     * @return A link to this resource's metadata.
     */
    @Override
    public Link uriMetadata() {
        return new Link(getLink(Links.METADATA));
    }


    /**
     * Link.
     *
     * @return A link to this resource's template.
     */
    @Override
    public Link uriTemplate() {
        return new Link(getLink(Links.TEMPLATE));
    }


    /**
     * Link.
     *
     * @return A link to this resource's duration.
     */
    @Override
    public Link duration() {
        return new Link(getLink(Links.DURATION));
    }


    /**
     * Link.
     *
     * @return A link to this resource.
     */
    @Override
    public final Link self() {
        return new Link(getLink(Links.SELF));
    }


    /**
     * Link.
     *
     * @return A link to this resource's name.
     */
    public Link rename() {
        return new Link(getLink(Links.NAME));
    }



    /**
     * Rel names for links on the resource class.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Links {
        private Links() { super(); }

        /** NAME : String. */
        public static final String NAME = "name";
        /** WC_APPLY : String. */
        public static final String WC = "wc";
        /** LIST : String. */
        public static final String LIST = "list";
        /** REVISIONS : String. */
        public static final String REVISIONS = "revisions";
        /** ABSOLUTE_PATH : String. */
        public static final String ABSOLUTE_PATH = "absolute-path";
        /** INCLUDE_MM : String. */
        public static final String INCLUDE_MM = "include_mm";
        /** LOCK : String. */
        public static final String LOCK = "lock";
        /** PARENT : String. */
        public static final String PARENT = "parent";
        /** DURATION : String. */
        public static final String DURATION = "duration";
        /** ACL : String. */
        public static final String ACL = "acl";
        /** PUBLISH : String. */
        public static final String PUBLISH = "publish";
        /** EXCLUDE_MM : String. */
        public static final String EXCLUDE_MM = "exclude_mm";
        /** METADATA : String. */
        public static final String METADATA = "metadata";
        /** TEMPLATE : String. */
        public static final String TEMPLATE = "template";
        /** SELF : String. */
        public static final String SELF = "self";
        /** DELETE : String. */
        public static final String DELETE = "delete";
        /** SEARCH : String. */
        public static final String SEARCH = "search";
    }
}
