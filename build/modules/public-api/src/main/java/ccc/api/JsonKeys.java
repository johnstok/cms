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
package ccc.api;


/**
 * Key names used for serializing CCC data types.
 *
 * <p><strong>
 * It is STRONGLY recommended that the values of keys be named conservatively.
 * This will maximise their portability across different serialisation formats,
 * such as JSON, XML, etc.</strong>
 *
 * <p>
 * Ideally, the existing style should be maintained, limiting keys to the
 * lower case a Latin alphabet and the hyphen. Always begin a key with a letter.
 *
 * @author Civic Computing Ltd.
 */
public final class JsonKeys {

    private JsonKeys() { super(); }

    /** TAGS : String. */
    public static final String TAGS = "tags";
    /** TEMPLATE_ID : String. */
    public static final String TEMPLATE_ID = "template-id";
    /** DATE_CHANGED : String. */
    public static final String DATE_CHANGED = "date-changed";
    /** DATE_CREATED : String. */
    public static final String DATE_CREATED = "date-created";
    /** HAS_WORKING_COPY : String. */
    public static final String HAS_WORKING_COPY = "has-working-copy";
    /** SORT_ORDER : String. */
    public static final String SORT_ORDER = "sort-order";
    /** SORT_LIST : String. */
    public static final String SORT_LIST = "sort-list";
    /** INCLUDE_IN_MAIN_MENU : String. */
    public static final String INCLUDE_IN_MAIN_MENU = "include-in-main-menu";
    /** FOLDER_COUNT : String. */
    public static final String FOLDER_COUNT = "folder-count";
    /** CHILD_COUNT : String. */
    public static final String CHILD_COUNT = "child-count";
    /** PUBLISHED_BY : String. */
    public static final String PUBLISHED_BY = "published-by";
    /** TITLE : String. */
    public static final String TITLE = "title";
    /** LOCKED_BY : String. */
    public static final String LOCKED_BY = "locked-by";
    /** TYPE : String. */
    public static final String TYPE = "type";
    /** PARENT_ID : String. */
    public static final String PARENT_ID = "parent-id";
    /** NAME : String. */
    public static final String NAME = "name";
    /** ID : String. */
    public static final String ID = "id";
    /** USERNAME : String. */
    public static final String USERNAME = "username";
    /** EMAIL : String. */
    public static final String EMAIL = "email";
    /** ROLES : String. */
    public static final String ROLES = "roles";
    /** PATH : String. */
    public static final String PATH = "path";
    /** METADATA : String. */
    public static final String METADATA = "metadata";
    /** SIZE : String. */
    public static final String SIZE = "size";
    /** DESCRIPTION : String. */
    public static final String DESCRIPTION = "description";
    /** DATA : String. */
    public static final String DATA = "data";
    /** MIME_TYPE : String. */
    public static final String MIME_TYPE = "mime-type";
    /** BODY : String. */
    public static final String BODY = "body";
    /** DEFINITION : String. */
    public static final String DEFINITION = "definition";
    /** TARGET : String. */
    public static final String TARGET = "target"; // FIXME: Should be 'target-id'?
    /** ABSOLUTE_PATH : String. */
    public static final String ABSOLUTE_PATH = "absolute-path";
    /** INDEX_PAGE_ID : String. */
    public static final String INDEX_PAGE_ID = "index-page-id";
    /** REVISION : String. */
    public static final String REVISION = "revision";
    /** UNLOCK : String. */
    public static final String UNLOCK = "unlock";
    /** UNPUBLISH : String. */
    public static final String UNPUBLISH = "unpublish";
    /** CACHE_DURATION : String. */
    public static final String CACHE_DURATION = "cache-duration";
    /** SECONDS : String. */
    public static final String SECONDS = "seconds";
    /** ACTION : String. */
    public static final String ACTION = "action"; // FIXME: Should be 'command'.
    /** ACTOR : String. */
    public static final String ACTOR = "actor"; // FIXME: Should be 'actor-id'?
    /** HAPPENED_ON : String. */
    public static final String HAPPENED_ON = "happened-on";
    /** MAJOR_CHANGE : String. */
    public static final String MAJOR_CHANGE = "major-change";
    /** INDEX : String. */
    public static final String INDEX = "index";
    /** COMMENT : String. */
    public static final String COMMENT = "comment";
    /** EXECUTE_AFTER : String. */
    public static final String EXECUTE_AFTER = "execute-after";
    /** SUBJECT_TYPE : String. */
    public static final String SUBJECT_TYPE = "subject-type";
    /** SUBJECT_PATH : String. */
    public static final String SUBJECT_PATH = "subject-path";
    /** STATUS : String. */
    public static final String STATUS = "status";
    /** DELTA : String. */
    public static final String DELTA = "delta";
    /** CODE : String. */
    public static final String CODE = "code";
    /** VERSION : String. */
    public static final String VERSION = "version";
    /** PASSWORD : String. */
    public static final String PASSWORD = "password";
    /** SUBJECT_ID : String. */
    public static final String SUBJECT_ID = "subject-id";
    /** PARAMETERS : String. */
    public static final String PARAMETERS = "parameters";
    /** FAILURE : String. */
    public static final String FAILURE = "failure";
}
