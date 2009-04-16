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
    private Action       _action;
    private Date         _happenedOn;
    private ResourceType _subjectType;
    private UUID         _subjectId;
    private String       _comment;
    private String       _detail;
    private boolean      _isMajorEdit;

    /** Valid actions for a log entry. */
    public static enum Action {
        /** RENAME : Action. */
        RENAME,
        /** MOVE : Action. */
        MOVE,
        /** PUBLISH : Action. */
        PUBLISH,
        /** UNPUBLISH : Action. */
        UNPUBLISH,
        /** CREATE : Action. */
        CREATE,
        /** UPDATE : Action. */
        UPDATE,
        /** LOCK : Action. */
        LOCK,
        /** UNLOCK : Action. */
        UNLOCK,
        /** CHANGE_TEMPLATE : Action. */
        CHANGE_TEMPLATE,
        /** UPDATE_TAGS : Action. */
        UPDATE_TAGS,
        /** INCLUDE_IN_MM : Action. */
        INCLUDE_IN_MM,
        /** REMOVE_FROM_MM : Action. */
        REMOVE_FROM_MM,
        /** UPDATE_METADATA : Action. */
        UPDATE_METADATA,
        /** CHANGE_ROLES : Action. */
        CHANGE_ROLES,
        /** REORDER : Action. */
        REORDER,
        /** UPDATE_SORT_ORDER : Action. */
        UPDATE_SORT_ORDER
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
        le._comment = "Renamed resource to '"+resource.name()+"'.";
        final Snapshot ss = new Snapshot();
        ss.set("name", resource.name().toString());
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
        le._action = Action.MOVE;
        le._comment = "Moved resource to parent: "
            +resource.parent().absolutePath()+".";
        final Snapshot ss = new Snapshot();
        ss.set("path", resource.absolutePath().toString());
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
        le._action = Action.UNLOCK;
        le._comment = "Unlocked.";
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
        le._action = Action.LOCK;
        le._comment = "Locked.";
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
        le._action = Action.CREATE;
        le._comment = "Created.";
        final Snapshot ss = resource.createSnapshot();
        ss.set("path", resource.absolutePath().toString());
        ss.set("name", resource.name().toString());
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
        le._action = Action.UPDATE;
        le._comment = (comment == null ? "Updated." : comment);
        le._isMajorEdit = isMajorEdit;
        final Snapshot ss = resource.createSnapshot();
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

        final LogEntry le = createEntry(resource, actor, happenedOn);
        le._action = Action.UPDATE;
        le._comment = "Updated.";
        final Snapshot ss = resource.createSnapshot();
        le._detail = ss.getDetail();
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
        le._comment = "Template changed.";
        final Snapshot ss = new Snapshot();
        String templateName = "";
        if (resource.template() != null) {
            templateName = resource.template().name().toString();
        }
        ss.set("template", templateName);
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
        le._action = Action.PUBLISH;
        le._comment = "Published.";
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
        le._action = Action.UNPUBLISH;
        le._comment = "Unpublished.";
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
        le._action = Action.UPDATE_TAGS;
        le._comment = "Updated tags.";
        final Snapshot ss = new Snapshot();
        ss.set("tags", resource.tagString());
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
        le._action = Action.UPDATE_METADATA;
        le._comment = "Updated metadata.";
        final Snapshot ss = new Snapshot();
        final StringBuilder sb = new StringBuilder();
        for (final String key : resource.metadata().keySet()) {
            if (sb.length() > 0) {
                sb.append(";");
            }
            sb.append(key);
            sb.append("=");
            sb.append(resource.metadata().get(key));
        }

        ss.set("metadata", sb.toString());
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
        le._action = Action.INCLUDE_IN_MM;
        le._comment = "Included in main menu.";
        final Snapshot ss = new Snapshot();
        ss.set("includeInMainMenu", resource.includeInMainMenu());
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
        le._action = Action.REMOVE_FROM_MM;
        le._comment = "Removed from main menu.";
        final Snapshot ss = new Snapshot();
        ss.set("includeInMainMenu", resource.includeInMainMenu());
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
        le._action = Action.CHANGE_ROLES;
        le._comment = "Roles changed.";
        final Snapshot ss = new Snapshot();
        final StringBuilder sb = new StringBuilder();
        for (final String role : resource.roles()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(role);
        }
        ss.set("roles", sb.toString());
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
                                      final User actor, final
                                      Date happenedOn) {

        final LogEntry le = createEntry(folder, actor, happenedOn);
        le._action = Action.REORDER;
        le._comment = "Reordered.";
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
                                              final User actor, final
                                              Date happenedOn) {

                final LogEntry le = createEntry(folder, actor, happenedOn);
                le._action = Action.UPDATE_SORT_ORDER;
                le._comment = "Updated sort order.";
                final Snapshot ss = new Snapshot();
                ss.set("sortOrder", folder.sortOrder().name());
                le._detail = ss.getDetail();
                return le;
    }

    private static LogEntry createEntry(final Resource resource,
                                        final User actor,
                                        final Date happenedOn) {

        DBC.require().notNull(resource);
        DBC.require().notNull(actor);
        DBC.require().notNull(happenedOn);

        final LogEntry le = new LogEntry();
        le._subjectId = resource.id();
        le._subjectType = resource.type();
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
