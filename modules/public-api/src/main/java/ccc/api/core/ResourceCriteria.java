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
import java.util.UUID;


/**
 * Criteria class for resource search.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceCriteria implements Serializable {

    private UUID _parent = null;
    private String _tag = null;
    private Date _changedAfter = null;
    private Date _changedBefore = null;
    private String _mainmenu = null;
    private String _Type = null;


    /**
     * Accessor.
     *
     * @return Returns the Type.
     */
    public final String getType() {

        return _Type;
    }


    /**
     * Mutator.
     *
     * @param Type The Type to set.
     */
    public final void setType(final String Type) {

        _Type = Type;
    }

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
     * @return The parent.
     */
    public UUID getParent() {
        return _parent;
    }

    /**
     * Accessor.
     *
     * @return The tag.
     */
    public String getTag() {
        return _tag;
    }

    /**
     * Accessor.
     *
     * @return Date changed before.
     */
    public Date getChangedBefore() {
        return _changedBefore;
    }

    /**
     * Mutator.
     *
     * @param date Date changed before.
     */
    public void setChangedBefore(final Date date) {
        _changedBefore = date;
    }
     /**
     * Accessor.
     *
     * @return Date changed after.
     */
    public Date getChangedAfter() {
        return _changedAfter;
    }
    /**
     * Mutator.
     *
     * @param date Date changed after.
     */
    public void setChangedAfter(final Date date) {
        _changedAfter = date;
    }
    /**
     * Mutator.
     *
     * @param tag The tag.
     */
    public void setTag(final String tag) {
        _tag = tag;
    }
    /**
     * Mutator.
     *
     * @param parent The resource's parent.
     */
    public void setParent(final UUID parent) {
        _parent = parent;
    }

    /**
     * Mutator.
     *
     * @param mainmenu Included in main menu.
     */
    public void setMainmenu(final String mainmenu) {
        _mainmenu = mainmenu;
    }

    /**
     * Accessor.
     *
     * @return  Included in main menu.
     */
    public String getMainmenu() {
        return _mainmenu;
    }

}
