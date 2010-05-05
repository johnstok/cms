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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ccc.api.core.Revision;
import ccc.api.types.CommandType;
import ccc.api.types.DBC;


/**
 * Abstract implementation of a resource revision.
 *
 * @author Civic Computing Ltd.
 * @param <T>
 */
public abstract class RevisionEntity<T> extends Entity {
    private Date _timestamp;
    private UserEntity _actor;
    private boolean _majorChange;
    private String _comment;

    /** Constructor: for persistence only. */
    protected RevisionEntity() { super(); }

    /**
     * Constructor.
     *
     * @param timestamp The timestamp of the revision.
     * @param actor The user causing the change.
     * @param majorChange The boolean flag of the major change.
     * @param comment The comment of the revision.
     */
    protected RevisionEntity(final Date timestamp,
                       final UserEntity actor,
                       final boolean majorChange,
                       final String comment) {
        DBC.require().notNull(timestamp);
        DBC.require().notNull(actor);

        _timestamp = timestamp;
        _actor = actor;
        _majorChange = majorChange;
        _comment = comment;
    }


    /**
     * Accessor.
     *
     * @return Returns the timestamp.
     */
    public final Date getTimestamp() {
        return new Date(_timestamp.getTime());
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public final UserEntity getActor() {
        return _actor;
    }


    /**
     * Accessor.
     *
     * @return Returns true if the version was a major change, false otherwise.
     */
    public final boolean isMajorChange() {
        return _majorChange;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public final String getComment() {
        return _comment;
    }

    /**
     * Retrieve the revision's state.
     *
     * @return A delta describing this revision's state.
     */
    protected abstract T delta();





    /**
     * Create a DTO for a revision.
     *
     * @param index The revision's index.
     *
     * @return A corresponding summary.
     */
    public Revision mapRevision(final int index) {
        return
            new Revision(
                CommandType.PAGE_UPDATE,
                getActor().getUsername(),
                getTimestamp(),
                index,
                getComment(),
                isMajorChange());
    }



    /**
     * Create DTOs for a collection of revisions.
     *
     * @param revisions The revisions.
     *
     * @return The corresponding summaries.
     */
    public static List<Revision> mapRevisions(
                         final Map<Integer, ? extends RevisionEntity<?>> revisions) {
        final List<Revision> mapped =
            new ArrayList<Revision>();
        for (final Map.Entry<Integer, ? extends RevisionEntity<?>> rev
            : revisions.entrySet()) {
            mapped.add(rev.getValue().mapRevision(rev.getKey().intValue()));
        }
        return mapped;
    }
}
