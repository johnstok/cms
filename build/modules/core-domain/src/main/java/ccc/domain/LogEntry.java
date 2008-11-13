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

import ccc.commons.DBC;
import ccc.commons.serialisation.Serializer;


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
        PUBLISH,
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
     * Create a log entry for the unlocking of a resource.
     *
     * @param resource The resource that was unlocked.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
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
     * Create a log entry for the locking of a resource.
     *
     * @param resource The resource that was locked.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forLock(final Resource resource,
                                   final User actor,
                                   final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = Action.LOCK;
        le._summary = "Locked.";
        return le;
    }


    /**
     * Create a log entry for the creation of a resource.
     *
     * @param resource The resource that was created.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forCreate(final Resource resource,
                                     final User actor,
                                     final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = Action.CREATE;
        le._summary = "Created.";
        return le;
    }



    /**
     * Create a log entry for the update of a resource.
     *
     * @param resource The resource that was updated.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forUpdate(final Resource resource,
                                     final User actor,
                                     final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = Action.UPDATE;
        le._summary = "Updated.";
        return le;
    }


    /**
     * Create a log entry for the change of a resource's template.
     *
     * @param resource The resource whose template changed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forTemplateChange(final Resource resource,
                                             final User actor,
                                             final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = Action.CHANGE_TEMPLATE;
        le._summary = "Template changed.";
        return le;
    }


    /** {@inheritDoc} */
    @Override public void serialize(final Serializer s) {
        s.number(   "index",                     _index);
        s.date(     "recordedOn",                _recordedOn);
        s.string(   "actor",                     _actor.username());
        s.string(   "action",                    _action.name());
        s.date(     "happenedOn",                _happenedOn);
        s.string(   "resourceType",              _subjectType.name());
        s.string(   "subjectId",                 _subjectId.toString());
        s.number(   "subjectVersionAfterChange", _subjectVersionAfterChange);
        s.string(   "summary",                   _summary);
    }


    private static LogEntry createEntry(final Resource p,
                                        final User actor,
                                        final Date happenedOn) {

        DBC.require().notNull(p);
        DBC.require().notNull(actor);
        DBC.require().notNull(happenedOn);

        final LogEntry le = new LogEntry();
        le._subjectId = p.id();
        le._subjectType = p.type();
        le._subjectVersionAfterChange = p.version();
        le._actor = actor;
        le._happenedOn = happenedOn;
        return le;
    }


    /**
     * Accessor.
     *
     * @return The uuid of the resource upon which the action was performed.
     */
    public UUID subjectId() {
        return _subjectId;
    }


    /**
     * Accessor.
     *
     * @return The type of resource upon which the action was performed.
     */
    public ResourceType subjectType() {
        return _subjectType;
    }


    /**
     * Accessor.
     *
     * @return The user that performed the action.
     */
    public User actor() {
        return _actor;
    }


    /**
     * Accessor.
     *
     * @return The action that was performed.
     */
    public Action action() {
        return _action;
    }


    /**
     * Accessor.
     *
     * @return The index of the log entry in the audit log.
     */
    public long index() {
        return _index;
    }


    /**
     * Accessor.
     *
     * @return The date that the log entry was recorded to the audit log.
     */
    public Date recordedOn() {
        return (null==_recordedOn) ? null : new Date(_recordedOn.getTime());
    }


    /**
     * Accessor.
     *
     * @return The date that the user performed the action.
     */
    public Date happenedOn() {
        return new Date(_happenedOn.getTime());
    }


    /**
     * Accessor.
     *
     * @return A summary of the action that was performed, as a string.
     */
    public String summary() {
        return _summary;
    }


    /**
     * Accessor.
     *
     * @return The version of the subject after the action was completed.
     */
    public int subjectVersionAfterChange() {
        return _subjectVersionAfterChange;
    }
}
