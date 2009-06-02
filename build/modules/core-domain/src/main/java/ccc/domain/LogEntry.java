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

import static ccc.api.DBC.*;

import java.util.Date;
import java.util.UUID;

import ccc.api.CommandType;
import ccc.api.Duration;
import ccc.api.JsonKeys;


/**
 * Captures detail relevant to a single user action for persistence in the audit
 * log.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntry extends Entity {

    private long         _index = -1;  // Only available once persisted
    private Date         _recordedOn;  // Only available once persisted

    private User         _actor;
    private CommandType  _action;
    private Date         _happenedOn;
    private UUID         _subjectId;
    private String       _comment = "";
    private String       _detail;
    private boolean      _isMajorEdit = false;

    /** Constructor: for persistence only. */
    protected LogEntry() { super(); }

    /**
     * Constructor.
     *
     * @param actor The actor that performed the action.
     * @param action The action performed.
     * @param happenedOn When the action took place.
     * @param subjectId The subject of the action.
     * @param comment A user supplied comment.
     * @param detail Details of the action.
     * @param isMajorEdit Was the action a major change.
     */
    public LogEntry(final User actor,
                    final CommandType action,
                    final Date happenedOn,
                    final UUID subjectId,
                    final String comment,
                    final String detail,
                    final boolean isMajorEdit) {
        require().notNull(subjectId);
        require().notNull(actor);
        require().notNull(happenedOn);
        require().notNull(action);

        _actor = actor;
        _action = action;
        _happenedOn = new Date(happenedOn.getTime());
        _subjectId = subjectId;
        _comment = (null==comment) ? "" : comment;
        _detail = detail;
        _isMajorEdit = isMajorEdit;
    }




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
        le._action = CommandType.RESOURCE_RENAME;
        final Snapshot ss = new Snapshot();
        ss.set(JsonKeys.NAME, resource.name().toString());
        le._detail = ss.getDetail();
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
        le._action = CommandType.RESOURCE_MOVE;
        final Snapshot ss = new Snapshot();
        ss.set(JsonKeys.PATH, resource.absolutePath().toString());
        ss.set(JsonKeys.PARENT_ID, resource.parent().id().toString());
        le._detail = ss.getDetail();
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
        le._action = CommandType.RESOURCE_UNLOCK;
        final Snapshot ss = new Snapshot();
        ss.set("unlock", actor.id().toString());
        le._detail = ss.getDetail();
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
        le._action = CommandType.RESOURCE_LOCK;
        final Snapshot ss = new Snapshot();
        ss.set("lock", actor.id().toString());
        le._detail = ss.getDetail();
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
        switch (resource.type()) {
            case ALIAS:
                le._action = CommandType.ALIAS_CREATE;
                break;
            case FILE:
                le._action = CommandType.FILE_CREATE;
                break;
            case FOLDER:
                le._action = CommandType.FOLDER_CREATE;
                break;
            case PAGE:
                le._action = CommandType.PAGE_CREATE;
                break;
            case SEARCH:
                le._action = CommandType.SEARCH_CREATE;
                break;
            case TEMPLATE:
                le._action = CommandType.TEMPLATE_CREATE;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        final Snapshot ss = new Snapshot(resource.createSnapshot());

        ss.set(JsonKeys.NAME, resource.name().toString());

        final User pBy = resource.publishedBy();
        ss.set(JsonKeys.PUBLISHED_BY, (null==pBy)?null:pBy.id().toString());

        final Resource parent = resource.parent();
        ss.set(JsonKeys.PARENT_ID, (null==parent)?null:parent.id().toString());

        le._detail = ss.getDetail();
        return le;
    }



    /**
     * Create a log entry for the update of a resource.
     *
     * @param resource The resource that was updated.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @param comment The comment from the user.
     * @param isMajorEdit A boolean for major edit.
     * @return The log entry representing the action.
     */
    public static LogEntry forUpdate(final Resource resource,
                                     final User actor,
                                     final Date happenedOn,
                                     final String comment,
                                     final boolean isMajorEdit) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        switch (resource.type()) {
            case ALIAS:
                le._action = CommandType.ALIAS_UPDATE;
                break;
            case FILE:
                le._action = CommandType.FILE_UPDATE;
                break;
            case FOLDER:
                le._action = CommandType.FOLDER_UPDATE;
                break;
            case PAGE:
                le._action = CommandType.PAGE_UPDATE;
                break;
            case TEMPLATE:
                le._action = CommandType.TEMPLATE_UPDATE;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        le._comment = (null==comment) ? "" : comment;
        require().containsNoBrackets(le._comment);

        le._isMajorEdit = isMajorEdit;
        final Snapshot ss = new Snapshot(resource.createSnapshot());
        le._detail = ss.getDetail();
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
        return forUpdate(resource, actor, happenedOn, "", false);
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
        le._action = CommandType.RESOURCE_CHANGE_TEMPLATE;
        final Snapshot ss = new Snapshot();
        final Template t = resource.template();
        ss.set(JsonKeys.TEMPLATE_ID, (null==t)?null:t.id().toString());
        le._detail = ss.getDetail();
        return le;
    }

    /**
     * Create a log entry for the publishing of a resource.
     *
     * @param resource The resource that was published.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forPublish(final Resource resource,
                                      final User actor,
                                      final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = CommandType.RESOURCE_PUBLISH;
        final Snapshot ss = new Snapshot();
        ss.set("publish", actor.id().toString());
        le._detail = ss.getDetail();
        return le;
    }

    /**
     * Create a log entry for the un-publishing of a resource.
     *
     * @param resource The resource that was unpublished.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forUnpublish(final Resource resource,
                                      final User actor,
                                      final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = CommandType.RESOURCE_UNPUBLISH;
        final Snapshot ss = new Snapshot();
        ss.set("unpublish", actor.id().toString());
        le._detail = ss.getDetail();
        return le;
    }

    /**
     * Create a log entry for the updating tags of a resource.
     *
     * @param resource The resource that was changed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forUpdateTags(final Resource resource,
                                         final User actor,
                                         final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = CommandType.RESOURCE_UPDATE_TAGS;
        final Snapshot ss = new Snapshot();
        ss.setStrings(JsonKeys.TAGS, resource.tags());
        le._detail = ss.getDetail();
        return le;
    }

    /**
     * Create a log entry for the updating metadata of a resource.
     *
     * @param resource The resource that was changed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forUpdateMetadata(final Resource resource,
                                         final User actor,
                                         final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = CommandType.RESOURCE_UPDATE_METADATA;
        final Snapshot ss = new Snapshot();
        ss.set(JsonKeys.METADATA, resource.metadata());
        le._detail = ss.getDetail();
        return le;
    }

    /**
     * Create a log entry for the including resource in the main menu.
     *
     * @param resource The resource that was changed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forIncludeInMainMenu(final Resource resource,
                                                final User actor,
                                                final Date happenedOn) {
        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = CommandType.RESOURCE_INCLUDE_IN_MM;
        final Snapshot ss = new Snapshot();
        ss.set(JsonKeys.INCLUDE_IN_MAIN_MENU,
               Boolean.valueOf(resource.includeInMainMenu()));
        le._detail = ss.getDetail();
        return le;
    }


    /**
     * Create a log entry for the removing a resource from the main menu.
     *
     * @param resource The resource that was changed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forRemoveFromMainMenu(final Resource resource,
                                                 final User actor,
                                                 final Date happenedOn) {
        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = CommandType.RESOURCE_REMOVE_FROM_MM;
        final Snapshot ss = new Snapshot();
        ss.set(JsonKeys.INCLUDE_IN_MAIN_MENU,
               Boolean.valueOf(resource.includeInMainMenu()));
        le._detail = ss.getDetail();
        return le;
    }


    /**
     * Create a log entry for the removing a resource from the main menu.
     *
     * @param resource The resource that was changed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forChangeRoles(final Resource resource,
                                          final User actor,
                                          final Date happenedOn) {
        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = CommandType.RESOURCE_CHANGE_ROLES;
        final Snapshot ss = new Snapshot();
        ss.setStrings("roles", resource.roles());
        le._detail = ss.getDetail();
        return le;
    }

    /**
     * Create a log entry for the reordering folder's resources.
     *
     * @param folder The folder that was changed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forReorder(final Folder folder,
                                      final User actor,
                                      final Date happenedOn) {

        final LogEntry le = createEntry(folder, actor, happenedOn);
        le._action = CommandType.FOLDER_REORDER;
        final Snapshot ss = new Snapshot();
        ss.set("reorder", actor.id().toString());
        le._detail = ss.getDetail();
        return le;
    }


    /**
     * Create a log entry for the changing folder's sort order.
     *
     * @param folder The folder that was changed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     */
    public static LogEntry forUpdateSortOrder(final Folder folder,
                                              final User actor,
                                              final Date happenedOn) {

        final LogEntry le = createEntry(folder, actor, happenedOn);
        le._action = CommandType.FOLDER_UPDATE_SORT_ORDER;
        final Snapshot ss = new Snapshot();
        ss.set(JsonKeys.SORT_ORDER, folder.sortOrder().name());
        le._detail = ss.getDetail();
        return le;
    }

    /**
     * Create a log entry to record a change to the resource's caching policy.
     *
     * @param resource The resource that was changed.
     * @param actor The actor that performed the action.
     * @param happenedOn The date that the actor performed the action.
     * @return The log entry representing the action.
     * @return
     */
    public static LogEntry forUpdateCache(final Resource resource,
                                          final User actor,
                                          final Date happenedOn) {

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = CommandType.RESOURCE_UPDATE_CACHE;
        final Snapshot ss = new Snapshot();
        final Duration d = resource.cache();
        ss.set("cache", (null==d) ? (Long) null : Long.valueOf(d.time()));
        le._detail = ss.getDetail();
        return le;
    }


    /**
     * Create a log entry for user password change.
     *
     * @param pw The password that changed.
     * @param actor The user that performed the action.
     * @param happenedOn When the action took place.
     * @return The log entry representing the action.
     */
    public static LogEntry forUserChangePassword(final Password pw,
                                                 final User actor,
                                                 final Date happenedOn) {
        final LogEntry le = createEntry(pw, actor, happenedOn);
        le._action = CommandType.USER_CHANGE_PASSWORD;
        final Snapshot ss = new Snapshot();
        le._detail = ss.getDetail();
        return le;
    }


    private static LogEntry createEntry(final Entity entity,
                                        final User actor,
                                        final Date happenedOn) {
        require().notNull(entity);
        require().notNull(actor);
        require().notNull(happenedOn);

        final LogEntry le = new LogEntry();
        le._subjectId = entity.id();
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
    public CommandType action() {
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
     * @return A comment of the action that was performed, as a string.
     */
    public String comment() {
        return _comment;
    }


    /**
     * Accessor.
     *
     * @return Details of the state of the object after the action took place.
     */
    public String detail() {
        return _detail;
    }

    /**
     * Accessor.
     *
     * @return Is major edit?
     */
    public boolean isMajorEdit() {
        return _isMajorEdit;
    }
}
