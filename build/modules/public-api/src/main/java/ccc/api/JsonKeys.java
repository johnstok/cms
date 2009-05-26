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
 * Key names used for serializing to JSON.
 *
 * @author Civic Computing Ltd.
 */
public final class JsonKeys {

    private JsonKeys() { super(); }

    /** TAGS : String. */
    public static final String TAGS = "tags";
    /** TEMPLATE_ID : String. */
    public static final String TEMPLATE_ID = "templateId";
    /** DATE_CHANGED : String. */
    public static final String DATE_CHANGED = "dateChanged";
    /** DATE_CREATED : String. */
    public static final String DATE_CREATED = "dateCreated";
    /** HAS_WORKING_COPY : String. */
    public static final String HAS_WORKING_COPY = "hasWorkingCopy";
    /** SORT_ORDER : String. */
    public static final String SORT_ORDER = "sortOrder";
    /** INCLUDE_IN_MAIN_MENU : String. */
    public static final String INCLUDE_IN_MAIN_MENU = "includeInMainMenu";
    /** FOLDER_COUNT : String. */
    public static final String FOLDER_COUNT = "folderCount";
    /** CHILD_COUNT : String. */
    public static final String CHILD_COUNT = "childCount";
    /** PUBLISHED_BY : String. */
    public static final String PUBLISHED_BY = "publishedBy";
    /** TITLE : String. */
    public static final String TITLE = "title";
    /** LOCKED_BY : String. */
    public static final String LOCKED_BY = "lockedBy";
    /** TYPE : String. */
    public static final String TYPE = "type";
    /** PARENT_ID : String. */
    public static final String PARENT_ID = "parentId";
    /** NAME : String. */
    public static final String NAME = "name";
    /** ID : String. */
    public static final String ID = "id";
}
