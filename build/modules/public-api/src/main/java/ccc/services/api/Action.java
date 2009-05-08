package ccc.services.api;

/** Valid actions for a log entry. */
public enum Action {
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
    UPDATE_SORT_ORDER,
    /** UPDATE_CACHE : Action. */
    UPDATE_CACHE
}