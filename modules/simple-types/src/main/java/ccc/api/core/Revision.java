/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import ccc.api.types.Username;


/**
 * A summary of a log entry.
 *
 * @author Civic Computing Ltd.
 */
public final class Revision
    implements
        Serializable {

    private Username _actorUsername;
    private Date _happenedOn;
    private long _index;
    private String _comment;
    private boolean _isMajor;


    /**
     * Constructor.
     */
    public Revision() { super(); }


    /**
     * Constructor.
     *
     * @param actorUsername The action's actor.
     * @param on The date of the action.
     * @param index The index of the action.
     * @param comment The comment of the action.
     * @param isMajorEdit Is the action a major edit.
     */
    public Revision(final Username  actorUsername,
                    final Date    on,
                    final long    index,
                    final String comment,
                    final boolean isMajorEdit) {
        _actorUsername = actorUsername;
        _happenedOn = new Date(on.getTime());
        _index = index;
        _comment = comment;
        _isMajor = isMajorEdit;
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public Username getActorUsername() {
        return _actorUsername;
    }


    /**
     * Accessor.
     *
     * @return Returns the happenedOn.
     */
    public Date getHappenedOn() {
        return new Date(_happenedOn.getTime());
    }


    /**
     * Accessor.
     *
     * @return Returns the index.
     */
    public long getIndex() {
        return _index;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public String getComment() {
        return _comment;
    }


    /**
     * Accessor.
     *
     * @return Returns the isMajor.
     */
    public boolean isMajor() {
        return _isMajor;
    }


    /**
     * Property names for this class.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Properties {
        private Properties() { super(); }

        /** USERNAME : String. */
        public static final String USERNAME      = "actorUsername";
        /** HAPPENED_ON : String. */
        public static final String HAPPENED_ON   = "happenedOn";
        /** COMMENT : String. */
        public static final String COMMENT       = "comment";
        /** IS_MAJOR_EDIT : String. */
        public static final String IS_MAJOR_EDIT = "major";
        /** INDEX : String. */
        public static final String INDEX         = "index";
    }
}
