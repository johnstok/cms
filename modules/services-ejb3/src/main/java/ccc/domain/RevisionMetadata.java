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
package ccc.domain;

import java.util.Date;


/**
 * Metadata describing a revision.
 *
 * @author Civic Computing Ltd.
 */
public final class RevisionMetadata {

    private final Date _timestamp;
    private final UserEntity _actor;
    private final boolean _majorChange;
    private final String _comment;


    /**
     * Constructor.
     *
     * @param timestamp The timestamp of the revision.
     * @param actor The user causing the change.
     * @param majorChange The boolean flag of the major change.
     * @param comment The comment of the revision.
     */
    public RevisionMetadata(final Date timestamp,
                            final UserEntity actor,
                            final boolean majorChange,
                            final String comment) {
        _timestamp = new Date(timestamp.getTime());
        _actor = actor;
        _majorChange = majorChange;
        _comment = comment;
    }


    /**
     * Accessor.
     *
     * @return Returns the timestamp.
     */
    public Date getTimestamp() {
        return new Date(_timestamp.getTime());
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public UserEntity getActor() {
        return _actor;
    }


    /**
     * Accessor.
     *
     * @return Returns the majorChange.
     */
    public boolean isMajorChange() {
        return _majorChange;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public String getComment() {
        return _comment;
    }
}
