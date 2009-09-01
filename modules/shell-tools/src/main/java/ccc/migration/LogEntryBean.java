/*-----------------------------------------------------------------------------

 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.rest.dto.UserSummary;


/**
 * A java bean representing a log entry record in CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntryBean {
    private int  _actor;
    private Date _happenedOn;
    private UserSummary _user;



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
    UserSummary getUser() {
        return _user;
    }


    /**
     * Mutator.
     *
     * @param user The user to set.
     */
    void setUser(final UserSummary user) {
        _user = user;
    }
}
