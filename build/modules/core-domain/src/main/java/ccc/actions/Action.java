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
package ccc.actions;

import java.util.Date;
import java.util.UUID;

import ccc.domain.Entity;
import ccc.domain.Snapshot;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Action extends Entity {
    private Date _executeAfter;
    private UUID _actor;
    private Type _type;
    private Snapshot _parameters;

    /**
     * Supported action types.
     *
     * @author Civic Computing Ltd.
     */
    public static enum Type {
        /** PUBLISH : Type. */
        PUBLISH,
        /** UNPUBLISH : Type. */
        UNPUBLISH;
    }

    /** Constructor: for persistence only. */
    protected Action() { super(); }

    /**
     * Constructor.
     *
     * @param unpublish
     * @param date
     * @param user
     * @param snapshot
     */
    public Action(final Type unpublish, final Date date, final UUID user, final Snapshot snapshot) {
        _type = unpublish;
        _executeAfter = date;
        _actor = user;
        _parameters = snapshot;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Type type() {
        return _type;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Snapshot parameters() {
        return _parameters;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public UUID actor() {
        return _actor;
    }
}
