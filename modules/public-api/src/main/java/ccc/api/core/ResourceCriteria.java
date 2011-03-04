/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;


/**
 * Criteria class for resource search.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceCriteria implements Serializable {

    private UUID _parent = null;
    private String _name = null;
    private String _tag = null;
    private Date _changedAfter = null;
    private Date _changedBefore = null;
    private Boolean _mainmenu = null;
    private ResourceType _type = null;
    private Boolean _published = true;
    private Boolean _locked = null;

    private String _sortField;
    private SortOrder _sortOrder;
    private Map<String, String> _metaMatches = new HashMap<String, String>();


    /**
     * Constructor.
     *
     */
    public ResourceCriteria() {
        super();
    }


    /**
     * Accessor.
     *
     * @return Returns the Type.
     */
    public final ResourceType getType() {
        return _type;
    }


    /**
     * Mutator.
     *
     * @param type The type to set.
     */
    public final void setType(final ResourceType type) {
        _type = type;
    }


    /**
     * Accessor.
     *
     * @return The parent.
     */
    public final UUID getParent() {
        return _parent;
    }

    /**
     * Accessor.
     *
     * @return The tag.
     */
    public final String getTag() {
        return _tag;
    }

    /**
     * Accessor.
     *
     * @return Date changed before.
     */
    public final Date getChangedBefore() {
        return
            (null==_changedBefore) ? null : new Date(_changedBefore.getTime());
    }

    /**
     * Mutator.
     *
     * @param date Date changed before.
     */
    public final void setChangedBefore(final Date date) {
        _changedBefore = (null==date) ? null : new Date(date.getTime());
    }
     /**
     * Accessor.
     *
     * @return Date changed after.
     */
    public final Date getChangedAfter() {
        return
            (null==_changedAfter) ? null : new Date(_changedAfter.getTime());
    }
    /**
     * Mutator.
     *
     * @param date Date changed after.
     */
    public final void setChangedAfter(final Date date) {
        _changedAfter =  (null==date) ? null : new Date(date.getTime());
    }
    /**
     * Mutator.
     *
     * @param tag The tag.
     */
    public final void setTag(final String tag) {
        _tag = tag;
    }
    /**
     * Mutator.
     *
     * @param parent The resource's parent.
     */
    public final void setParent(final UUID parent) {
        _parent = parent;
    }

    /**
     * Mutator.
     *
     * @param mainmenu Included in main menu.
     */
    public final void setMainmenu(final Boolean mainmenu) {
        _mainmenu = mainmenu;
    }

    /**
     * Accessor.
     *
     * @return  Included in main menu.
     */
    public final Boolean getMainmenu() {
        return _mainmenu;
    }


    /**
     * Accessor.
     *
     * @return Returns the published.
     */
    public final Boolean getPublished() {
        return _published;
    }



    /**
     * Mutator.
     *
     * @param published The published to set.
     */
    public final void setPublished(final Boolean published) {
        _published = published;
    }



    /**
     * Accessor.
     *
     * @return Returns the locked.
     */
    public final Boolean getLocked() {
        return _locked;
    }



    /**
     * Mutator.
     *
     * @param locked The locked to set.
     */
    public final void setLocked(final Boolean locked) {
        _locked = locked;
    }


    /**
     * Sort the results.
     *
     * @param field The paragraph to sort on.
     * @param order The order to sort.
     */
    public final void sort(final String field, final SortOrder order) {
        _sortField = field;
        _sortOrder = order;
    }


    /**
     * Accessor.
     *
     * @return The order by which results will be sorted.
     */
    public final SortOrder getSortOrder() {
        return _sortOrder;
    }


    /**
     * Accessor.
     *
     * @return The field on which to sort results.
     */
    public final String getSortField() {
        return _sortField;
    }


    /**
     * Mutator.
     *
     * @param sortField The field to sort on.
     */
    public final void setSortField(final String sortField) {
        _sortField = sortField;
    }


    /**
     * Mutator.
     *
     * @param sortOrder The order to sort in.
     */
    public final void setSortOrder(final SortOrder sortOrder) {
        _sortOrder = sortOrder;
    }


    /**
     * Match the specified metadatum.
     *
     * @param name The metadatum name.
     * @param value The metadatum value.
     */
    public final void matchMetadatum(final String name, final String value) {
        _metaMatches.put(name, value);
    }


    /**
     * Accessor.
     *
     * @return The metadata to match.
     */
    public final Map<String, String> getMetadata() {
        return new HashMap<String, String>(_metaMatches);
    }


    /**
     * Mutator.
     *
     * @param metadata The metadata to set.
     */
    public final void setMetadata(final Map<String, String> metadata) {
        _metaMatches = new HashMap<String, String>(metadata);
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }


    /**
     * Mutator.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        _name = name;
    }
}
