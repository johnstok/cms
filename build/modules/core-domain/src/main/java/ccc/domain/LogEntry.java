/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
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
import java.util.UUID;


/**
 * Captures detail relevant to a single user action for persistence in the audit
 * log.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntry extends Entity {

    private long         _index = -1;  // TODO: Only available once persisted
    private Date         _recordedOn;  // TODO: Only available once persisted

    private User         _actor;
    private Action       _action;
    private Date         _happenedOn;
    private ResourceType _subjectType;
    private UUID         _subjectId;
    private int          _subjectVersionAfterChange;
    private String       _summary;

    /** Valid actions for a log entry. */
    public static enum Action {
        RENAME,
        MOVE,
        UNPUBLISH,
        CREATE,
        UPDATE,
        LOCK,
        UNLOCK,
        CHANGE_TEMPLATE
    }


    /** Constructor: for persistence only. */
    protected LogEntry() { super(); }


    /**
     * Create a log entry for the rename of a resource.
     *
     * @param resource The resource that was renamed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forRename(final Resource resource,
                                     final User actor,
                                     final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = Action.RENAME;
        le._summary = "Renamed resource to '"+resource.name()+"'.";
        return le;
    }



    /**
     * Create a log entry for the rename of a resource.
     *
     * @param resource The resource that was renamed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forMove(final Resource resource,
                                   final User actor,
                                   final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = Action.MOVE;
        le._summary = "Moved resource to parent: "+resource.parent().id()+".";
        return le;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param p
     * @param _actor2
     * @param on
     * @return
     */
    public static LogEntry forUnlock(final Resource resource,
                                     final User actor,
                                     final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = Action.UNLOCK;
        le._summary = "Unlocked.";
        return le;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param p
     * @param _actor2
     * @param on
     * @return
     */
    public static LogEntry forLock(final Resource resource,
                                   final User actor,
                                   final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = Action.LOCK;
        le._summary = "Locked.";
        return le;
    }


    private static LogEntry createEntry(final Resource p,
                                        final User actor,
                                        final Date happenedOn) {

        final LogEntry le = new LogEntry();
        le._subjectId = p.id();
        le._subjectType = p.type();
        le._subjectVersionAfterChange = p.version();
        le._actor = actor;
        le._happenedOn = happenedOn;
        return le;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public UUID subjectId() {
        return _subjectId;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ResourceType subjectType() {
        return _subjectType;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public User actor() {
        return _actor;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Action action() {
        return _action;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public long index() {
        return _index;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Date recordedOn() {
        return _recordedOn;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Date happenedOn() {
        return _happenedOn;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String summary() {
        return _summary;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public int subjectVersionAfterChange() {
        return _subjectVersionAfterChange;
    }
}
