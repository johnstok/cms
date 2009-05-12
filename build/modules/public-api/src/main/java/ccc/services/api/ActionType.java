package ccc.services.api;

/** Valid actions for a log entry. */
public enum ActionType {
    /** RENAME : ActionType. */
    RENAME,
    /** MOVE : ActionType. */
    MOVE,
    /** PUBLISH : ActionType. */
    PUBLISH,
    /** UNPUBLISH : ActionType. */
    UNPUBLISH,
    /** CREATE : ActionType. */
    CREATE,
    /** UPDATE : ActionType. */
    UPDATE,
    /** LOCK : ActionType. */
    LOCK,
    /** UNLOCK : ActionType. */
    UNLOCK,
    /** CHANGE_TEMPLATE : ActionType. */
    CHANGE_TEMPLATE,
    /** UPDATE_TAGS : ActionType. */
    UPDATE_TAGS,
    /** INCLUDE_IN_MM : ActionType. */
    INCLUDE_IN_MM,
    /** REMOVE_FROM_MM : ActionType. */
    REMOVE_FROM_MM,
    /** UPDATE_METADATA : ActionType. */
    UPDATE_METADATA,
    /** CHANGE_ROLES : ActionType. */
    CHANGE_ROLES,
    /** REORDER : ActionType. */
    REORDER,
    /** UPDATE_SORT_ORDER : ActionType. */
    UPDATE_SORT_ORDER,
    /** UPDATE_CACHE : ActionType. */
    UPDATE_CACHE,
    /** CLEAR_WC : ActionType. */
    CLEAR_WC
}