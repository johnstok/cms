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
package ccc.migration;

import java.util.Date;

import ccc.rest.dto.UserDto;


/**
 * A java bean representing a log entry record in CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntryBean {
    private int  _actor;
    private Date _happenedOn;
    private UserDto _user;



    /**
     * Constructor.
     *
     * @param actor The actor that performed the action.
     * @param happenedOn The date the action happened.
     */
    public LogEntryBean(final int actor, final Date happenedOn) {
        _actor = actor;
        _happenedOn = new Date(happenedOn.getTime());
    }

    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public int getActor() {
        return _actor;
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
     * @return Returns the user.
     */
    UserDto getUser() {
        return _user;
    }


    /**
     * Mutator.
     *
     * @param user The user to set.
     */
    void setUser(final UserDto user) {
        _user = user;
    }
}
